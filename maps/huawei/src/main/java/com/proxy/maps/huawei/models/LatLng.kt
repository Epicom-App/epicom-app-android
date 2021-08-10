package com.proxy.maps.huawei.models

import com.proxy.maps.model.LatLng
import com.huawei.hms.maps.model.LatLng as HuaweiLatLng

internal fun HuaweiLatLng.toLatLng() = LatLng(this.latitude, this.longitude)

internal fun LatLng.toHuaweiLatLng() = HuaweiLatLng(this.latitude, this.longitude)
