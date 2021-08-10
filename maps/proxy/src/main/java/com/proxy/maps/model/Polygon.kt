package com.proxy.maps.model


interface Polygon {

    val id: String
    val points: List<LatLng>

    val holes: List<List<LatLng>>

    val strokeWidth: Float
    val strokeColor: Int
    val strokeJointType: Int
    val strokePattern: List<PatternItem>
    val fillColor: Int
    val zIndex: Float
    val isVisible: Boolean
    val isGeodesic: Boolean
    val isClickable: Boolean
    var tag: Any?

    fun setHoles(holes: List<MutableList<LatLng>>)
    fun remove()
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

}