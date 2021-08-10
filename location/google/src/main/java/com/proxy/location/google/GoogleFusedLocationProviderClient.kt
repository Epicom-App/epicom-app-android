package com.proxy.location.google

import android.app.PendingIntent
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.Keep
import androidx.annotation.RequiresPermission
import com.proxy.location.CancellationToken
import com.proxy.location.FusedLocationProviderClient
import com.proxy.location.LocationAvailability
import com.proxy.location.LocationCallback
import com.proxy.location.LocationRequest
import com.proxy.location.Task
import com.proxy.location.google.tasks.toTask
import com.google.android.gms.location.FusedLocationProviderClient as GoogleClient
import com.google.android.gms.location.LocationServices as GoogleLocationServices

@Keep
internal class GoogleFusedLocationProviderClient(
    context: Context
) : FusedLocationProviderClient {

    private val googleClient: GoogleClient =
        GoogleLocationServices.getFusedLocationProviderClient(context)

    override val lastLocation: Task<Location?>
        @RequiresPermission(
            anyOf = [
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION"
            ]
        )
        get() = googleClient.lastLocation.toTask()

    override val looper: Looper
        get() = googleClient.looper

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun getCurrentLocation(
        priority: Int,
        cancellationToken: CancellationToken
    ): Task<Location?> = googleClient.getCurrentLocation(
        priority,
        cancellationToken.toGoogleCancellationToken()
    ).toTask()

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun getLocationAvailability(): Task<LocationAvailability> =
        googleClient.locationAvailability.toTask()

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun requestLocationUpdates(
        locationRequest: LocationRequest,
        pendingIntent: PendingIntent
    ): Task<Void> =
        googleClient.requestLocationUpdates(
            locationRequest.toGoogleLocationRequest(),
            pendingIntent
        ).toTask()

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback,
        looper: Looper
    ): Task<Void> =
        googleClient.requestLocationUpdates(
            locationRequest.toGoogleLocationRequest(),
            locationCallback.toGoogleLocationCallback(),
            looper
        ).toTask()

    override fun removeLocationUpdates(locationCallback: LocationCallback): Task<Void> =
        googleClient.removeLocationUpdates(
            locationCallback.toGoogleLocationCallback()
        ).toTask()

    override fun removeLocationUpdates(pendingIntent: PendingIntent): Task<Void> =
        googleClient.removeLocationUpdates(pendingIntent).toTask()

    override fun flushLocations(): Task<Void> =
        googleClient.flushLocations().toTask()
}
