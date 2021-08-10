package com.proxy.maps.huawei.models

import com.proxy.maps.model.LatLng
import com.proxy.maps.model.LatLngBounds
import com.proxy.maps.model.VisibleRegion
import com.huawei.hms.maps.model.VisibleRegion as HuaweiVisibleRegion

internal fun HuaweiVisibleRegion.toVisibleRegion(): VisibleRegion {
    val huaweiVisibleRegion = this
    return object : VisibleRegion {

        override val farLeft: LatLng
            get() = huaweiVisibleRegion.farLeft.toLatLng()

        override val farRight: LatLng
            get() = huaweiVisibleRegion.farRight.toLatLng()

        override val latLngBounds: LatLngBounds
            get() = huaweiVisibleRegion.latLngBounds.toLatLngBounds()

        override val nearLeft: LatLng
            get() = huaweiVisibleRegion.nearLeft.toLatLng()

        override val nearRight: LatLng
            get() = huaweiVisibleRegion.nearRight.toLatLng()

        override fun equals(other: Any?): Boolean = huaweiVisibleRegion == other

        override fun hashCode(): Int = huaweiVisibleRegion.hashCode()

        override fun toString(): String = huaweiVisibleRegion.toString()
    }
}
