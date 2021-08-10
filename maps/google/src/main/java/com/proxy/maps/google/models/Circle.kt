package com.proxy.maps.google.models

import com.proxy.maps.model.Circle
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.PatternItem
import com.google.android.gms.maps.model.Circle as GoogleCircle
import com.google.android.gms.maps.model.PatternItem as GooglePatternItem

@Suppress("VarCouldBeVal")
internal fun GoogleCircle.toCircle(): Circle {
    val googleCircle = this
    return object : Circle {

        override var center: LatLng
            get() = googleCircle.center.toLatLng()
            set(value) {
                googleCircle.center = value.toGoogleLatLng()
            }

        override var fillColor: Int
            get() = googleCircle.fillColor
            set(value) {
                googleCircle.fillColor = value
            }

        override var radius: Double
            get() = googleCircle.radius
            set(value) {
                googleCircle.radius = value
            }
        override var strokeColor: Int
            get() = googleCircle.strokeColor
            set(value) {
                googleCircle.strokeColor = value
            }
        override var strokePattern: List<PatternItem>?
            get() = googleCircle.strokePattern?.map(GooglePatternItem::toPatternItem)
            set(value) {
                googleCircle.strokePattern = value?.map(PatternItem::toGooglePatternItem)
            }
        override var strokeWidth: Float
            get() = googleCircle.strokeWidth
            set(value) {
                googleCircle.strokeWidth = value
            }
        override var tag: Any?
            get() = googleCircle.tag
            set(value) {
                googleCircle.tag = value
            }
        override var zIndex: Float
            get() = googleCircle.zIndex
            set(value) {
                googleCircle.zIndex = value
            }
        override var isClickable: Boolean
            get() = googleCircle.isClickable
            set(value) {
                googleCircle.isClickable = value
            }
        override var isVisible: Boolean
            get() = googleCircle.isVisible
            set(value) {
                googleCircle.isVisible = value
            }

        override fun getId(): String = googleCircle.id
        override fun remove() = googleCircle.remove()
        override fun equals(other: Any?): Boolean = googleCircle == other
        override fun hashCode(): Int = googleCircle.hashCode()
    }
}
