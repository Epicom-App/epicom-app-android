package com.proxy.maps.google.models

import com.proxy.maps.model.PolylineOptions
import com.google.android.gms.maps.model.PolylineOptions as GooglePolylineOptions

fun PolylineOptions.toGooglePolylineOptions() : GooglePolylineOptions {
    val options = this
    return with(GooglePolylineOptions()) {
        width(options.width)
        color(options.color)
        zIndex(options.zIndex)
        visible(options.isVisible)
        geodesic(options.isGeodesic)
        clickable(options.isClickable)
        jointType(options.jointType)
        addAll(options.points.map { it.toGoogleLatLng() })
        startCap(options.startCap)
        endCap(options.endCap)
        pattern(options.pattern)
        this
    }
}