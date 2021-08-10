package com.proxy.maps.google.models

import com.proxy.maps.model.DelegateLatLngBoundsBuilder
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.LatLngBounds
import com.google.android.gms.maps.model.LatLngBounds as GLatLngBounds
import com.google.android.gms.maps.model.LatLngBounds.Builder as GLatLngBoundsBuilder

internal data class GoogleLatLngBounds(
    private val googleLatLngBounds: GLatLngBounds
) : LatLngBounds {

    override val southwest: LatLng
        get() = googleLatLngBounds.southwest.toLatLng()

    override val northeast: LatLng
        get() = googleLatLngBounds.northeast.toLatLng()

    override fun contains(position: LatLng?) =
        googleLatLngBounds.contains(position?.toGoogleLatLng())

    override fun including(point: LatLng) =
        googleLatLngBounds.including(point.toGoogleLatLng()).toLatLngBounds()

    override val center: LatLng
        get() = googleLatLngBounds.center.toLatLng()

    override fun equals(other: Any?): Boolean = googleLatLngBounds == other
    override fun hashCode(): Int = googleLatLngBounds.hashCode()
    override fun toString(): String = googleLatLngBounds.toString()
}

class GoogleDelegateLatLngBoundsBuilder : DelegateLatLngBoundsBuilder {

    private val googleLatLngBoundsBuilder = GLatLngBoundsBuilder()

    override fun include(latLng: LatLng): DelegateLatLngBoundsBuilder {
        googleLatLngBoundsBuilder.include(latLng.toGoogleLatLng())
        return this
    }

    override fun build(): LatLngBounds = GoogleLatLngBounds(googleLatLngBoundsBuilder.build())
}

fun GLatLngBounds.toLatLngBounds(): LatLngBounds =
    GoogleLatLngBounds(GLatLngBounds(southwest, northeast))

fun LatLngBounds.toGoogleLatLngBounds() = GLatLngBounds(
    this.southwest.toGoogleLatLng(),
    this.northeast.toGoogleLatLng()
)
