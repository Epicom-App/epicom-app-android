package com.proxy.maps.huawei.models

import com.proxy.maps.model.LatLng
import com.proxy.maps.model.PatternItem
import com.proxy.maps.model.Polygon
import com.huawei.hms.maps.model.Polygon as HuaweiPolygon

fun HuaweiPolygon.toPolygon(): Polygon {
    val googlePolygon = this
    return object : Polygon {
        override val id: String
            get() = googlePolygon.id
        override val points: List<LatLng>
            get() = googlePolygon.points.map { it.toLatLng() }
        override val holes: List<List<LatLng>>
            get() = googlePolygon.holes.map { hole -> hole.map { it.toLatLng() } }
        override val strokeWidth: Float
            get() = googlePolygon.strokeWidth
        override val strokeColor: Int
            get() = googlePolygon.strokeColor
        override val strokeJointType: Int
            get() = googlePolygon.strokeJointType
        override val strokePattern: List<PatternItem>
            get() = googlePolygon.strokePattern.map { it.toPatternItem() }
        override val fillColor: Int
            get() = googlePolygon.fillColor
        override val zIndex: Float
            get() = googlePolygon.zIndex
        override val isVisible: Boolean
            get() = googlePolygon.isVisible
        override val isGeodesic: Boolean
            get() = googlePolygon.isGeodesic
        override val isClickable: Boolean
            get() = googlePolygon.isClickable
        override var tag: Any?
            get() = googlePolygon.tag
            set(value) {
                googlePolygon.tag = value
            }

        override fun setHoles(holes: List<MutableList<LatLng>>) {
            googlePolygon.holes = holes.map { hole -> hole.map { it.toHuaweiLatLng() } }
        }

        override fun remove() {
            googlePolygon.remove()
        }

        override fun equals(other: Any?): Boolean =
            (other is Polygon && other.id == googlePolygon.id)

        override fun hashCode(): Int = googlePolygon.hashCode()

    }
}