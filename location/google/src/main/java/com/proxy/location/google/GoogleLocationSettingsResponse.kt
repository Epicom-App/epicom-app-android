package com.proxy.location.google

import com.google.android.gms.location.LocationSettingsResponse as GoogleLocationSettingsResponse
import com.proxy.location.LocationSettingsResponse
import com.proxy.location.LocationSettingsStates

fun GoogleLocationSettingsResponse.toLocationSettingsResponse() : LocationSettingsResponse {
    val googleLocationSettingsResponse = this
    return object : LocationSettingsResponse {
        override fun getLocationSettingsStates(): LocationSettingsStates {
            return googleLocationSettingsResponse.locationSettingsStates.toLocationSettingsStates()
        }
    }
}
