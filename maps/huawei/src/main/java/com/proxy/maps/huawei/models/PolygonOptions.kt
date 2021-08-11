package com.proxy.maps.huawei.models

import com.proxy.maps.model.PolygonOptions
import com.huawei.hms.maps.model.PolygonOptions as GooglePolygonOptions

fun PolygonOptions.toHuaweiPolygonOptions(): GooglePolygonOptions {
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
        addAll(options.points.map { it.toHuaweiLatLng() })
        options.holes.forEach { hole ->
            addHole(hole.map { it.toHuaweiLatLng() }.asIterable())
        }
        strokePattern(options.strokePattern.map { it.toHuaweiPatternItem() })
        this
    }
}