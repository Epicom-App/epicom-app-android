package com.proxy.maps

import android.content.Context
import java.util.ServiceLoader

interface DelegateMapsInitializer {
    fun initialize(applicationContext: Context)
}

object MapsInitializer :
    DelegateMapsInitializer by
    ServiceLoader.load(DelegateMapsInitializer::class.java).single()
