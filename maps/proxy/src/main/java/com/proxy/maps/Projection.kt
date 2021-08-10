package com.proxy.maps

import android.graphics.Point
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.VisibleRegion

interface Projection {
    val visibleRegion: VisibleRegion?
    fun fromScreenLocation(point: Point): LatLng
    fun toScreenLocation(location: LatLng): Point
}
