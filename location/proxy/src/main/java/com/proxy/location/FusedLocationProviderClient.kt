package com.proxy.location

import android.app.PendingIntent
import android.location.Location
import android.os.Looper

interface FusedLocationProviderClient : TrivagoApi {

    val lastLocation: Task<Location?>

    fun getCurrentLocation(
        priority: Int,
        cancellationToken: CancellationToken
    ): Task<Location?>

    fun getLocationAvailability(): Task<LocationAvailability>

    fun removeLocationUpdates(locationCallback: LocationCallback): Task<Void>
    fun removeLocationUpdates(pendingIntent: PendingIntent): Task<Void>

    fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback,
        looper: Looper
    ): Task<Void>

    fun requestLocationUpdates(
        locationRequest: LocationRequest,
        pendingIntent: PendingIntent
    ): Task<Void>

    fun flushLocations(): Task<Void>
}
