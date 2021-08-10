package com.proxy.maps.huawei.models

import com.proxy.maps.model.DelegateLatLngBoundsBuilder
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.LatLngBounds
import com.huawei.hms.maps.model.LatLngBounds as HLatLngBounds
import com.huawei.hms.maps.model.LatLngBounds.Builder as HLatLngBoundsBuilder

internal data class HuaweiLatLngBounds(
    private val huaweiLatLngBounds: HLatLngBounds
) : LatLngBounds {

    override val southwest: LatLng
        get() = huaweiLatLngBounds.southwest.toLatLng()

    override val northeast: LatLng
        get() = huaweiLatLngBounds.northeast.toLatLng()

    override fun contains(position: LatLng?) =
        huaweiLatLngBounds.contains(position?.toHuaweiLatLng())

    override fun including(point: LatLng) =
        huaweiLatLngBounds.including(point.toHuaweiLatLng()).toLatLngBounds()

    override val center: LatLng
        get() = huaweiLatLngBounds.center.toLatLng()

    override fun equals(other: Any?): Boolean = huaweiLatLngBounds == other
    override fun hashCode(): Int = huaweiLatLngBounds.hashCode()
    override fun toString(): String = huaweiLatLngBounds.toString()
}

class HuaweiDelegateLatLngBoundsBuilder : DelegateLatLngBoundsBuilder {

    private val huaweiLatLngBoundsBuilder = HLatLngBoundsBuilder()

    override fun include(latLng: LatLng): DelegateLatLngBoundsBuilder {
        huaweiLatLngBoundsBuilder.include(latLng.toHuaweiLatLng())
        return this
    }

    override fun build(): LatLngBounds = HuaweiLatLngBounds(huaweiLatLngBoundsBuilder.build())
}

fun HLatLngBounds.toLatLngBounds(): LatLngBounds =
    HuaweiLatLngBounds(HLatLngBounds(southwest, northeast))

fun LatLngBounds.toHuaweiLatLngBounds() = HLatLngBounds(
    this.southwest.toHuaweiLatLng(),
    this.northeast.toHuaweiLatLng()
)
