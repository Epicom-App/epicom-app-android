package com.proxy.maps.model

class PolylineOptions(
    var width: Float = 10.0f,
    var color: Int = -16777216,
    var zIndex: Float = 0.0f,
    var isVisible: Boolean = true,
    var isGeodesic: Boolean = false,
    var isClickable: Boolean = false,
    var jointType: Int = 0,
    val points: List<LatLng> = emptyList(),
    val startCap: Cap = Cap(),
    val endCap: Cap = Cap(),
    val pattern: List<PatternItem> = emptyList()
) {

    fun add(var1: LatLng?): PolylineOptions {
        return this
    }

    fun add(vararg var1: LatLng?): PolylineOptions {
        return this
    }

    fun addAll(var1: Iterable<LatLng?>): PolylineOptions {
        return this
    }

    fun width(var1: Float): PolylineOptions {
        width = var1
        return this
    }

    fun color(var1: Int): PolylineOptions {
        color = var1
        return this
    }

    fun startCap(var1: Cap): PolylineOptions {
        return this
    }

    fun endCap(var1: Cap): PolylineOptions {
        return this
    }

    fun jointType(var1: Int): PolylineOptions {
        jointType = var1
        return this
    }

    fun pattern(var1: List<PatternItem>?): PolylineOptions {
        return this
    }

    fun zIndex(var1: Float): PolylineOptions {
        zIndex = var1
        return this
    }

    fun visible(var1: Boolean): PolylineOptions {
        isVisible = var1
        return this
    }

    fun geodesic(var1: Boolean): PolylineOptions {
        isGeodesic = var1
        return this
    }

    fun clickable(var1: Boolean): PolylineOptions {
        isClickable = var1
        return this
    }
}