package com.proxy.location

interface LocationSettingsResult {
    fun getLocationSettingsStates(): LocationSettingsStates
    fun getStatus(): Status
}
