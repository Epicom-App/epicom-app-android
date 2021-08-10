package com.proxy.location

interface LocationSettingsResponse {
    fun getLocationSettingsStates(): LocationSettingsStates
}
