@file:SuppressWarnings("MagicNumber")
package org.ebolapp.features.geotracking.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.features.permissions.usecases.HasPermissionUseCase
import java.util.concurrent.TimeUnit

interface LocationService {
    fun startBackgroundCollection()
    fun stopBackgroundCollection()
    suspend fun getLastKnownLocation() : Location?
    fun locationUpdates() : Flow<Location?>
}

internal class LocationServiceImpl(
    private val context: Context,
    private val hasPermissionUseCase: HasPermissionUseCase
) : LocationService {

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Background updates request
    private val backgroundLocationsRequest = LocationRequest().apply {
        fastestInterval = TimeUnit.MINUTES.toMillis(1)
        interval = TimeUnit.MINUTES.toMillis(5)
        maxWaitTime = TimeUnit.MINUTES.toMillis(15)
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private val backgroundLocationUpdatesPendingIntent: PendingIntent by lazy {
        val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    // Foreground updates request
    private val foregroundLocationRequest = LocationRequest().apply {
        fastestInterval = TimeUnit.SECONDS.toMillis(1)
        interval = TimeUnit.SECONDS.toMillis(5)
        maxWaitTime = TimeUnit.SECONDS.toMillis(5)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private var backgroundJob: Job? = null
        private set(value) {
            field?.cancel()
            field = value
        }

    @SuppressLint("MissingPermission")
    override fun startBackgroundCollection() {
        backgroundJob = GlobalScope.launch {
            hasPermissionUseCase(Permission.BACKGROUND_LOCATION).collect {hasPermission ->
                if (hasPermission) {
                    fusedLocationProviderClient.removeLocationUpdates(
                        backgroundLocationUpdatesPendingIntent
                    )
                    fusedLocationProviderClient.requestLocationUpdates(
                        backgroundLocationsRequest,
                        backgroundLocationUpdatesPendingIntent
                    )
                } else {
                    fusedLocationProviderClient.removeLocationUpdates(
                        backgroundLocationUpdatesPendingIntent
                    )
                }
            }
        }.also {
            it.invokeOnCompletion {
                fusedLocationProviderClient.removeLocationUpdates(
                    backgroundLocationUpdatesPendingIntent
                )
            }
        }
    }

    override fun stopBackgroundCollection() {
        backgroundJob = null
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation() = withContext(Dispatchers.IO) {
        if (hasPermissionUseCase(Permission.FOREGROUND_LOCATION).first())
            fusedLocationProviderClient.lastLocation.await()
        else
            null
    }

    @SuppressLint("MissingPermission")
    override fun locationUpdates() = channelFlow {

        if (!hasPermissionUseCase(Permission.FOREGROUND_LOCATION).first()) cancel()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { offerCatching(it) }
            }
        }

        fusedLocationProviderClient.lastLocation.await<Location?>()?.let { offerCatching(it) }
        fusedLocationProviderClient.requestLocationUpdates(
            foregroundLocationRequest,
            locationCallback,
            null
        ).await()

        awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
    }

    private fun <E> SendChannel<E>.offerCatching(element: E): Boolean {
        return runCatching { offer(element) }.getOrDefault(false)
    }
}

