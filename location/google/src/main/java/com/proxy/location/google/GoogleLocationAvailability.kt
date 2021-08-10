package com.proxy.location.google

import com.proxy.location.LocationAvailability
import com.google.android.gms.location.LocationAvailability as GoogleLocationAvailability

internal fun GoogleLocationAvailability.toLocationAvailability() = object : LocationAvailability {
    override fun isLocationAvailable() = this@toLocationAvailability.isLocationAvailable
}
