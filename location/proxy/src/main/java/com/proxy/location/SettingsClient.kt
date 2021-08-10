package com.proxy.location

interface SettingsClient {
    fun checkLocationSettings(
        locationSettingsRequest: LocationSettingsRequest
    ): Task<LocationSettingsResponse>
}
