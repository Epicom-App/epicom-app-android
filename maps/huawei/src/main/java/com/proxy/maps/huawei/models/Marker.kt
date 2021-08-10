package com.proxy.maps.huawei.models

import com.proxy.maps.model.BitmapDescriptor
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.Marker
import com.huawei.hms.maps.model.Marker as HuaweiMarker

@Suppress("VarCouldBeVal")
internal fun HuaweiMarker.toMarker(): Marker {
    val huaweiMarker = this
    return object : Marker {

        override var tag: Any?
            get() = huaweiMarker.tag
            set(value) {
                huaweiMarker.tag = value
            }

        override var position: LatLng
            get() = huaweiMarker.position.toLatLng()
            set(value) {
                huaweiMarker.position = value.toHuaweiLatLng()
            }

        override var zIndex: Float
            get() = huaweiMarker.zIndex
            set(value) {
                huaweiMarker.zIndex = value
            }

        override var alpha: Float
            get() = huaweiMarker.alpha
            set(value) {
                huaweiMarker.alpha = value
            }

        override var rotation: Float
            get() = huaweiMarker.rotation
            set(value) {
                huaweiMarker.rotation = value
            }

        override var snippet: String?
            get() = huaweiMarker.snippet
            set(value) {
                huaweiMarker.snippet = value
            }

        override var title: String?
            get() = huaweiMarker.title
            set(value) {
                huaweiMarker.title = value
            }

        override var isDraggable: Boolean
            get() = huaweiMarker.isDraggable
            set(value) {
                huaweiMarker.isDraggable = value
            }

        override var isFlat: Boolean
            get() = huaweiMarker.isFlat
            set(value) {
                huaweiMarker.isFlat = value
            }

        override var isVisible: Boolean
            get() = huaweiMarker.isVisible
            set(value) {
                huaweiMarker.isVisible = value
            }

        override fun getId(): String? = huaweiMarker.id

        override fun setAnchor(anchorU: Float, anchorV: Float) =
            huaweiMarker.setAnchor(anchorU, anchorV)

        override fun isInfoWindowShown(): Boolean = huaweiMarker.isInfoWindowShown

        override fun hideInfoWindow() = huaweiMarker.hideInfoWindow()

        override fun setInfoWindowAnchor(anchorU: Float, anchorV: Float) =
            huaweiMarker.setInfoWindowAnchor(anchorU, anchorV)

        override fun setIcon(bitmapDescriptor: BitmapDescriptor?) =
            huaweiMarker.setIcon(bitmapDescriptor.toHuaweiBitmapDescriptor())

        override fun remove() = huaweiMarker.remove()
        override fun equals(other: Any?): Boolean = huaweiMarker == other
        override fun hashCode(): Int = huaweiMarker.hashCode()
    }
}
