package com.proxy.maps.google.models

import com.proxy.maps.model.LatLng
import com.google.android.gms.maps.model.LatLng as GoogleLatLng

internal fun GoogleLatLng.toLatLng() = LatLng(this.latitude, this.longitude)

internal fun LatLng.toGoogleLatLng() = GoogleLatLng(this.latitude, this.longitude)
