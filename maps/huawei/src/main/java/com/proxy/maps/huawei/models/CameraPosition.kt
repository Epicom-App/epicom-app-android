package com.proxy.maps.huawei.models

import com.proxy.maps.model.CameraPosition
import com.huawei.hms.maps.model.CameraPosition as HuaweiCameraPosition

internal fun HuaweiCameraPosition.toCameraPosition() =
    CameraPosition(
        target = target.toLatLng(),
        zoom = zoom,
        tilt = tilt,
        bearing = bearing
    )

internal fun CameraPosition.toHuaweiCameraPosition() =
    HuaweiCameraPosition(
        target.toHuaweiLatLng(),
        zoom,
        tilt,
        bearing
    )
