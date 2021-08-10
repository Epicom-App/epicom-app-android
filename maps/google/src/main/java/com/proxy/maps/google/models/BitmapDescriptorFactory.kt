package com.proxy.maps.google.models

import android.graphics.Bitmap
import com.proxy.maps.model.DelegateBitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory as GoogleBitmapDescriptorFactory

class BitmapDescriptorFactory : DelegateBitmapDescriptorFactory {
    override fun fromBitmap(image: Bitmap?) =
        BitmapDescriptorWrapper(GoogleBitmapDescriptorFactory.fromBitmap(image))
}
