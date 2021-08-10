package com.proxy.maps.huawei.models

import android.graphics.Point
import com.proxy.maps.Projection
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.VisibleRegion
import com.huawei.hms.maps.Projection as HuaweiProjection

internal fun HuaweiProjection.toProjection(): Projection {
    val huaweiProjection = this
    return object : Projection {

        override val visibleRegion: VisibleRegion
            get() = huaweiProjection.visibleRegion.toVisibleRegion()

        override fun fromScreenLocation(point: Point): LatLng =
            huaweiProjection.fromScreenLocation(point).toLatLng()

        override fun toScreenLocation(location: LatLng): Point =
            huaweiProjection.toScreenLocation(location.toHuaweiLatLng())
    }
}
