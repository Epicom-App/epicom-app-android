package com.proxy.location

import android.content.Intent

interface LocationSettingsStates {
    fun isBlePresent(): Boolean
    fun isBleUsable(): Boolean
    fun isGpsPresent(): Boolean
    fun isGpsUsable(): Boolean
    fun isLocationPresent(): Boolean
    fun isLocationUsable(): Boolean
    fun isNetworkLocationPresent(): Boolean
    fun isNetworkLocationUsable(): Boolean
    companion object {
       fun fromIntent(intent: Intent) {

       }
    }
}

