package com.proxy.maps.huawei.models

import com.proxy.maps.model.MarkerOptions
import com.huawei.hms.maps.model.MarkerOptions as HuaweiMarkerOptions

internal fun MarkerOptions.toHuaweiMarkerOptions(): HuaweiMarkerOptions {
    val options = this
    return with(HuaweiMarkerOptions()) {
        alpha(options.alpha)
        anchor(options.anchorU, options.anchorV)
        flat(options.flat)
        icon(options.icon.toHuaweiBitmapDescriptor())
        infoWindowAnchor(options.infoWindowAnchorU, options.infoWindowAnchorV)
        position(
            options.position?.toHuaweiLatLng()
                ?: throw NullPointerException("Position should not be null")
        )
        rotation(options.rotation)
        snippet(options.snippet)
        title(options.title)
        zIndex(options.zIndex)
        draggable(options.draggable)
        visible(options.visible)
        this
    }
}
