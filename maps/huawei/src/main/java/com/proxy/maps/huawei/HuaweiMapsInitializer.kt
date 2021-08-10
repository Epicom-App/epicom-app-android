package com.proxy.maps.huawei

import android.content.Context
import com.huawei.hms.maps.MapsInitializer
import com.proxy.maps.DelegateMapsInitializer

internal class HuaweiMapsInitializer : DelegateMapsInitializer {
    override fun initialize(applicationContext: Context) {
        MapsInitializer.initialize(applicationContext)
    }
}
