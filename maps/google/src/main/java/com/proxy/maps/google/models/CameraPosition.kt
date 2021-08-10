package com.proxy.maps.google.models

import com.proxy.maps.model.CameraPosition
import com.google.android.gms.maps.model.CameraPosition as GoogleCameraPosition

internal fun GoogleCameraPosition.toCameraPosition() =
    CameraPosition(
        target = target.toLatLng(),
        zoom = zoom,
        tilt = tilt,
        bearing = bearing
    )

internal fun CameraPosition.toGoogleCameraPosition() =
    GoogleCameraPosition(
        target.toGoogleLatLng(),
        zoom,
        tilt,
        bearing
    )
