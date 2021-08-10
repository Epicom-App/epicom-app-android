package com.proxy.maps.huawei

import com.proxy.maps.CameraUpdate
import com.huawei.hms.maps.CameraUpdate as HuaweiCameraUpdate

internal class CameraUpdateWrapper(val huaweiCameraUpdate: HuaweiCameraUpdate) : CameraUpdate
