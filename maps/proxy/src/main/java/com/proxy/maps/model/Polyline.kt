package com.proxy.maps.model

interface Polyline {

    val id: String
    val points: List<LatLng>
    val width: Float

    val color: Int
    val startCap: Cap
    val endCap: Cap
    val jointType: Int

    val pattern: List<PatternItem>
    val zIndex: Float

    val isVisible: Boolean
    val isGeodesic: Boolean
    val isClickable: Boolean
    var tag: Any?

    fun remove()

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

}