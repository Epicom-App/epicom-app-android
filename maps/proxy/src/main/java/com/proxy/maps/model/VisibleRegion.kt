package com.proxy.maps.model

interface VisibleRegion {

    val farLeft: LatLng?
    val farRight: LatLng?
    val latLngBounds: LatLngBounds?
    val nearLeft: LatLng?
    val nearRight: LatLng?

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String
}
