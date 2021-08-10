package com.proxy.maps.google.models

import com.proxy.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptor as GoogleBitmapDescriptor

class BitmapDescriptorWrapper(val bitmapDescriptor: GoogleBitmapDescriptor) : BitmapDescriptor

internal fun BitmapDescriptor?.toGoogleBitmapDescriptor(): GoogleBitmapDescriptor? =
    (this as? BitmapDescriptorWrapper)?.bitmapDescriptor
