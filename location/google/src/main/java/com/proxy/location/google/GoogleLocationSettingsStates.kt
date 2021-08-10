package com.proxy.location.google

import com.google.android.gms.location.LocationSettingsStates as GoogleLocationSettingsStates
import com.proxy.location.LocationSettingsStates

fun GoogleLocationSettingsStates?.toLocationSettingsStates(): LocationSettingsStates {
    val googleLocationSettingsStates = this
    return object : LocationSettingsStates {

        override fun isBlePresent() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isBlePresent
        }

        override fun isBleUsable() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isBleUsable
        }

        override fun isGpsPresent() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isGpsPresent
        }

        override fun isGpsUsable() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isGpsUsable
        }

        override fun isLocationPresent() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isLocationPresent
        }

        override fun isLocationUsable() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isLocationUsable
        }

        override fun isNetworkLocationPresent() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isNetworkLocationPresent
        }

        override fun isNetworkLocationUsable() = when (googleLocationSettingsStates) {
            null -> false
            else -> googleLocationSettingsStates.isNetworkLocationUsable
        }
    }
}
