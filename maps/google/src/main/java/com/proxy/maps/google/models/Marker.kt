package com.proxy.maps.google.models

import com.proxy.maps.model.BitmapDescriptor
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.Marker
import com.google.android.gms.maps.model.Marker as GoogleMarker

@Suppress("VarCouldBeVal")
internal fun GoogleMarker.toMarker(): Marker {
    val googleMarker = this
    return object : Marker {

        override var tag: Any?
            get() = googleMarker.tag
            set(value) {
                googleMarker.tag = value
            }

        override var position: LatLng
            get() = googleMarker.position.toLatLng()
            set(value) {
                googleMarker.position = value.toGoogleLatLng()
            }

        override var zIndex: Float
            get() = googleMarker.zIndex
            set(value) {
                googleMarker.zIndex = value
            }

        override var alpha: Float
            get() = googleMarker.alpha
            set(value) {
                googleMarker.alpha = value
            }

        override var rotation: Float
            get() = googleMarker.rotation
            set(value) {
                googleMarker.rotation = value
            }

        override var snippet: String?
            get() = googleMarker.snippet
            set(value) {
                googleMarker.snippet = value
            }

        override var title: String?
            get() = googleMarker.title
            set(value) {
                googleMarker.title = value
            }

        override var isDraggable: Boolean
            get() = googleMarker.isDraggable
            set(value) {
                googleMarker.isDraggable = value
            }

        override var isFlat: Boolean
            get() = googleMarker.isFlat
            set(value) {
                googleMarker.isFlat = value
            }

        override var isVisible: Boolean
            get() = googleMarker.isVisible
            set(value) {
                googleMarker.isVisible = value
            }

        override fun getId(): String? = googleMarker.id

        override fun setAnchor(anchorU: Float, anchorV: Float) =
            googleMarker.setAnchor(anchorU, anchorV)

        override fun isInfoWindowShown(): Boolean = googleMarker.isInfoWindowShown

        override fun hideInfoWindow() = googleMarker.hideInfoWindow()

        override fun setInfoWindowAnchor(anchorU: Float, anchorV: Float) =
            googleMarker.setInfoWindowAnchor(anchorU, anchorV)

        override fun setIcon(bitmapDescriptor: BitmapDescriptor?) =
            googleMarker.setIcon(bitmapDescriptor.toGoogleBitmapDescriptor())

        override fun remove() = googleMarker.remove()
        override fun equals(other: Any?): Boolean = googleMarker == other
        override fun hashCode(): Int = googleMarker.hashCode()
    }
}
