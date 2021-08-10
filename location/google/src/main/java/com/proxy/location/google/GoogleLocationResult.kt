package com.proxy.location.google

import android.location.Location
import com.proxy.location.LocationResult
import com.google.android.gms.location.LocationResult as GoogleLocationResult

internal fun GoogleLocationResult.toLocationResult() = object : LocationResult {
    override val lastLocation: Location
        get() = this@toLocationResult.lastLocation

    override fun getLocations() = this@toLocationResult.locations
}
