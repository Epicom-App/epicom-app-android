package com.proxy.maps.huawei.models

import com.proxy.maps.model.PolylineOptions
import com.huawei.hms.maps.model.PolylineOptions as GooglePolylineOptions

fun PolylineOptions.toHuaweiPolylineOptions() : GooglePolylineOptions {
    val options = this
    return with(GooglePolylineOptions()) {
        width(options.width)
        color(options.color)
        zIndex(options.zIndex)
        visible(options.isVisible)
        geodesic(options.isGeodesic)
        clickable(options.isClickable)
        jointType(options.jointType)
        addAll(options.points.map { it.toHuaweiLatLng() })
        startCap(options.startCap)
        endCap(options.endCap)
        pattern(options.pattern)
        this
    }
}