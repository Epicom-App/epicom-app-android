package com.proxy.maps.google.models

import com.proxy.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolygonOptions as GooglePolygonOptions

fun PolygonOptions.toGooglePolygonOptions(): GooglePolygonOptions {
    val options = this
    return with(GooglePolygonOptions()) {
        strokeWidth(options.strokeWidth)
        strokeColor(options.strokeColor)
        fillColor(options.fillColor)
        zIndex(options.zIndex)
        visible(options.isVisible)
        geodesic(options.isGeodesic)
        clickable(options.isClickable)
        strokeJointType(options.strokeJointType)
        addAll(options.points.map { it.toGoogleLatLng() })
        options.holes.forEach { hole ->
            addHole(hole.map { it.toGoogleLatLng() }.asIterable())
        }
        strokePattern(options.strokePattern.map { it.toGooglePatternItem() })
        this
    }
}