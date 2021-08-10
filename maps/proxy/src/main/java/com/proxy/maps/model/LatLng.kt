package com.proxy.maps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LatLng(val latitude: Double, val longitude: Double) : Parcelable
