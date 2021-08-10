package com.proxy.maps.model

interface Circle {

    val center: LatLng
    val fillColor: Int
    val radius: Double
    val strokeColor: Int
    val strokePattern: List<PatternItem>?
    val strokeWidth: Float
    val tag: Any?
    val zIndex: Float
    val isClickable: Boolean
    val isVisible: Boolean

    fun getId(): String
    fun remove()

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}
