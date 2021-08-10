package com.proxy.maps.model

import java.util.ServiceLoader

interface LatLngBounds {

    val southwest: LatLng
    val northeast: LatLng
    val center: LatLng

    fun contains(position: LatLng?): Boolean
    fun including(point: LatLng): LatLngBounds

    interface Builder {
        fun include(latLng: LatLng): Builder
        fun build(): LatLngBounds
    }

    companion object {
        @Suppress("FunctionNaming")
        fun Builder(): Builder =
            ServiceLoader.load(DelegateLatLngBoundsBuilder::class.java).single()
    }

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String
}

interface DelegateLatLngBoundsBuilder : LatLngBounds.Builder
