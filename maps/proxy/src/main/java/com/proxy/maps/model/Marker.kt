package com.proxy.maps.model

interface Marker {
    var isVisible: Boolean
    var tag: Any?
    var position: LatLng
    var zIndex: Float
    var alpha: Float
    var rotation: Float
    var snippet: String?
    var title: String?
    var isDraggable: Boolean
    var isFlat: Boolean

    fun getId(): String?
    fun setAnchor(anchorU: Float, anchorV: Float)
    fun isInfoWindowShown(): Boolean
    fun hideInfoWindow()
    fun setInfoWindowAnchor(anchorU: Float, anchorV: Float)
    fun setIcon(bitmapDescriptor: BitmapDescriptor?)
    fun remove()

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}
