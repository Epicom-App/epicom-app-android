package com.proxy.maps.google.models

import com.proxy.maps.model.MarkerOptions
import com.google.android.gms.maps.model.MarkerOptions as GoogleMarkerOptions

internal fun MarkerOptions.toGoogleMarkerOptions(): GoogleMarkerOptions {
    val options = this
    return with(GoogleMarkerOptions()) {
        alpha(options.alpha)
        anchor(options.anchorU, options.anchorV)
        flat(options.flat)
        icon(options.icon.toGoogleBitmapDescriptor())
        infoWindowAnchor(options.infoWindowAnchorU, options.infoWindowAnchorV)
        position(
            options.position?.toGoogleLatLng()
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
