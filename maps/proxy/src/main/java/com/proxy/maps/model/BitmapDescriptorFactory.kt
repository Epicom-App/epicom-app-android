package com.proxy.maps.model

import android.graphics.Bitmap
import java.util.ServiceLoader

interface DelegateBitmapDescriptorFactory {
    fun fromBitmap(image: Bitmap?): BitmapDescriptor
}

object BitmapDescriptorFactory :
    DelegateBitmapDescriptorFactory by
    ServiceLoader
        .load(DelegateBitmapDescriptorFactory::class.java)
        .single()
