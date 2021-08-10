package com.proxy.maps.google

import com.proxy.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdate as GoogleCameraUpdate

internal class CameraUpdateWrapper(val googleCameraUpdate: GoogleCameraUpdate) : CameraUpdate
