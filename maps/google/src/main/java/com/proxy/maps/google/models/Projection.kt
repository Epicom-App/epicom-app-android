package com.proxy.maps.google.models

import android.graphics.Point
import com.proxy.maps.Projection
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.VisibleRegion
import com.google.android.gms.maps.Projection as GoogleProjection

internal fun GoogleProjection.toProjection(): Projection {
    val googleProjection = this
    return object : Projection {

        override val visibleRegion: VisibleRegion
            get() = googleProjection.visibleRegion.toVisibleRegion()

        override fun fromScreenLocation(point: Point): LatLng =
            googleProjection.fromScreenLocation(point).toLatLng()

        override fun toScreenLocation(location: LatLng): Point =
            googleProjection.toScreenLocation(location.toGoogleLatLng())
    }
}
