package com.proxy.maps.huawei.models

import com.proxy.maps.model.BitmapDescriptor
import com.huawei.hms.maps.model.BitmapDescriptor as HuaweiBitmapDescriptor

class BitmapDescriptorWrapper(val bitmapDescriptor: HuaweiBitmapDescriptor) : BitmapDescriptor

internal fun BitmapDescriptor?.toHuaweiBitmapDescriptor(): HuaweiBitmapDescriptor? =
    (this as? BitmapDescriptorWrapper)?.bitmapDescriptor
