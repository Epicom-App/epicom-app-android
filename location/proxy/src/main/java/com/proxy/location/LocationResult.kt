package com.proxy.location

import android.location.Location

interface LocationResult {
    val lastLocation: Location
    fun getLocations(): List<Location>
}
