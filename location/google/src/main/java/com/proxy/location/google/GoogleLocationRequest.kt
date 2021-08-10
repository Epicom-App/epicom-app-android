package com.proxy.location.google

import com.proxy.location.LocationRequest
import com.google.android.gms.location.LocationRequest as GoogleLocationRequest

internal fun LocationRequest.toGoogleLocationRequest(): GoogleLocationRequest {
    val locationRequest = this
    return GoogleLocationRequest().apply {
        setExpirationDuration(expirationTime)
        priority = locationRequest.priority
        interval = locationRequest.interval
        maxWaitTime = locationRequest.maxWaitTime
        fastestInterval = locationRequest.fastestInterval
        expirationTime = locationRequest.expirationTime
        setNumUpdates = locationRequest.setNumUpdates
        smallestDisplacement = locationRequest.smallestDisplacement
    }
}
