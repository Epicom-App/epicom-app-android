package com.proxy.location

interface LocationCallback {
    fun onLocationResult(result: LocationResult?) {}
    fun onLocationAvailability(availability: LocationAvailability?) {}
}
