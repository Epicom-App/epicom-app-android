package com.proxy.maps.huawei.models

import com.proxy.maps.model.Circle
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.PatternItem
import com.huawei.hms.maps.model.Circle as HuaweiCircle
import com.huawei.hms.maps.model.PatternItem as HuaweiPatternItem

@Suppress("VarCouldBeVal")
internal fun HuaweiCircle.toCircle(): Circle {
    val huaweiCircle = this
    return object : Circle {

        override var center: LatLng
            get() = huaweiCircle.center.toLatLng()
            set(value) {
                huaweiCircle.center = value.toHuaweiLatLng()
            }

        override var fillColor: Int
            get() = huaweiCircle.fillColor
            set(value) {
                huaweiCircle.fillColor = value
            }

        override var radius: Double
            get() = huaweiCircle.radius
            set(value) {
                huaweiCircle.radius = value
            }

        override var strokeColor: Int
            get() = huaweiCircle.strokeColor
            set(value) {
                huaweiCircle.strokeColor = value
            }

        override var strokePattern: List<PatternItem>?
            get() = huaweiCircle.strokePattern?.map(HuaweiPatternItem::toPatternItem)
            set(value) {
                huaweiCircle.strokePattern = value?.map(PatternItem::toHuaweiPatternItem)
            }

        override var strokeWidth: Float
            get() = huaweiCircle.strokeWidth
            set(value) {
                huaweiCircle.strokeWidth = value
            }

        override var tag: Any?
            get() = huaweiCircle.tag
            set(value) {
                huaweiCircle.setTag(value)
            }

        override var zIndex: Float
            get() = huaweiCircle.zIndex
            set(value) {
                huaweiCircle.zIndex = value
            }

        override var isClickable: Boolean
            get() = huaweiCircle.isClickable
            set(value) {
                huaweiCircle.isClickable = value
            }

        override var isVisible: Boolean
            get() = huaweiCircle.isVisible
            set(value) {
                huaweiCircle.isVisible = value
            }

        override fun getId(): String = huaweiCircle.id
        override fun remove() = huaweiCircle.remove()
        override fun equals(other: Any?): Boolean = huaweiCircle == other
        override fun hashCode(): Int = huaweiCircle.hashCode()
    }
}
