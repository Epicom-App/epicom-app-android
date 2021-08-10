package com.proxy.maps.google.models

import com.proxy.maps.model.LatLng
import com.proxy.maps.model.LatLngBounds
import com.proxy.maps.model.VisibleRegion
import com.google.android.gms.maps.model.VisibleRegion as GoogleVisibleRegion

internal fun GoogleVisibleRegion.toVisibleRegion(): VisibleRegion {
    val googleVisibleRegion = this
    return object : VisibleRegion {

        override val farLeft: LatLng
            get() = googleVisibleRegion.farLeft.toLatLng()

        override val farRight: LatLng
            get() = googleVisibleRegion.farRight.toLatLng()

        override val latLngBounds: LatLngBounds
            get() = googleVisibleRegion.latLngBounds.toLatLngBounds()

        override val nearLeft: LatLng
            get() = googleVisibleRegion.nearLeft.toLatLng()

        override val nearRight: LatLng
            get() = googleVisibleRegion.nearRight.toLatLng()

        override fun equals(other: Any?): Boolean = googleVisibleRegion == other

        override fun hashCode(): Int = googleVisibleRegion.hashCode()

        override fun toString(): String = googleVisibleRegion.toString()
    }
}
