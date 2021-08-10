package com.proxy.location.google

import com.google.android.gms.location.SettingsClient as GoogleSettingsClient
import com.proxy.location.LocationSettingsRequest
import com.proxy.location.SettingsClient
import com.proxy.location.google.tasks.toTask

fun GoogleSettingsClient.toSettingsClient(): SettingsClient {
    val googleSettingsClient = this
    return object : SettingsClient {
        override fun checkLocationSettings(
            locationSettingsRequest: LocationSettingsRequest
        ) = googleSettingsClient.checkLocationSettings(
            locationSettingsRequest.toGoogleLocationSettingsRequest()
        ).toTask()
    }
}
