package com.proxy.maps

interface UiSettings {

    var isMapToolbarEnabled: Boolean
    var isZoomControlsEnabled: Boolean
    var isIndoorLevelPickerEnabled: Boolean
    var isCompassEnabled: Boolean
    var isMyLocationButtonEnabled: Boolean

    fun setAllGesturesEnabled(enabled: Boolean)
}
