package com.proxy.maps.huawei.models


import com.proxy.maps.model.Cap
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.PatternItem
import com.proxy.maps.model.Polyline
import com.huawei.hms.maps.model.Polyline as HuaweiPolyline

fun HuaweiPolyline.toPolyline(): Polyline {
    val googlePolyline = this
    return object : Polyline {
        override val id: String
            get() = googlePolyline.id
        override val points: List<LatLng>
            get() = googlePolyline.points.map { it.toLatLng() }
        override val width: Float
            get() = googlePolyline.width
        override val color: Int
            get() = googlePolyline.color
        override val startCap: Cap
            get() = TODO()
        override val endCap: Cap
            get() = TODO("Not yet implemented")
        override val jointType: Int
            get() = googlePolyline.jointType
        override val pattern: List<PatternItem>
            get() = googlePolyline.pattern.map { it.toPatternItem() }
        override val zIndex: Float
            get() = googlePolyline.zIndex
        override val isVisible: Boolean
            get() = googlePolyline.isVisible
        override val isGeodesic: Boolean
            get() = googlePolyline.isGeodesic
        override val isClickable: Boolean
            get() = googlePolyline.isClickable
        override var tag: Any?
            get() = googlePolyline.tag
            set(value) { googlePolyline.tag = value }

        override fun remove() {
            googlePolyline.remove()
        }

        override fun equals(other: Any?): Boolean =
            (other is Polyline && other.id == googlePolyline.id)

        override fun hashCode(): Int = googlePolyline.hashCode()

    }
}