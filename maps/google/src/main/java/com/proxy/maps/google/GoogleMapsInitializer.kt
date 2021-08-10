package com.proxy.maps.google

import android.content.Context
import com.proxy.maps.DelegateMapsInitializer
import com.google.android.gms.maps.MapsInitializer as GoogleInitializer

internal class GoogleMapsInitializer : DelegateMapsInitializer {
    override fun initialize(applicationContext: Context) {
        GoogleInitializer.initialize(applicationContext)
    }
}
