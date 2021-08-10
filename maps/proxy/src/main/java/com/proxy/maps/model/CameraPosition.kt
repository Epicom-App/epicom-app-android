package com.proxy.maps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CameraPosition(
    val target: LatLng,
    val zoom: Float,
    val tilt: Float,
    val bearing: Float
) : Parcelable
