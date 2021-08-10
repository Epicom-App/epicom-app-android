package com.proxy.location.google

import com.proxy.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsRequest as GoogleLocationSettingsRequest

fun LocationSettingsRequest.toGoogleLocationSettingsRequest(): GoogleLocationSettingsRequest {
    val builder = GoogleLocationSettingsRequest.Builder()
    locationRequests.forEach {
        builder.addLocationRequest(it.toGoogleLocationRequest())
    }
    return builder.build()
}
