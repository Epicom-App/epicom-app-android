package com.proxy.maps.huawei.models

import android.graphics.Bitmap
import com.proxy.maps.model.DelegateBitmapDescriptorFactory
import com.huawei.hms.maps.model.BitmapDescriptorFactory as HuaweiBitmapDescriptorFactory

class BitmapDescriptorFactory : DelegateBitmapDescriptorFactory {
    override fun fromBitmap(image: Bitmap?) =
        BitmapDescriptorWrapper(HuaweiBitmapDescriptorFactory.fromBitmap(image))
}
