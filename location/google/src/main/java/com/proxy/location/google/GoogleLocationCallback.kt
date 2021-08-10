package com.proxy.location.google

import com.proxy.location.LocationCallback
import com.google.android.gms.location.LocationAvailability as GoogleLocationAvailability
import com.google.android.gms.location.LocationCallback as GoogleLocationCallback
import com.google.android.gms.location.LocationResult as GoogleLocationResult

internal fun LocationCallback.toGoogleLocationCallback(): GoogleLocationCallback {
    val locationCallback = this
    return object : GoogleLocationCallback() {
        override fun onLocationResult(locationResult: GoogleLocationResult) {
            locationCallback.onLocationResult(locationResult.toLocationResult())
        }

        override fun onLocationAvailability(locationAvailability: GoogleLocationAvailability?) {
            locationCallback.onLocationAvailability(locationAvailability?.toLocationAvailability())
        }
    }
}
