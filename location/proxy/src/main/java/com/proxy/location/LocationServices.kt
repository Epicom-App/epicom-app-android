package com.proxy.location

import android.app.Activity
import android.content.Context
import java.util.*

interface LocationServicesDelegates {
    fun fusedLocationProviderClientClassName(): String
    fun isPlatformServiceEnabled(applicationContext: Context): Boolean
    fun getPlatformProvider(): PlatformProvider
    fun getSettingsClient(activity: Activity): SettingsClient
}

sealed class PlatformProvider {
    object Huawei : PlatformProvider()
    object Google : PlatformProvider()
}

object LocationServices {

    private val platformDelegate by lazy {
        ServiceLoader.load(LocationServicesDelegates::class.java).single()
    }

    fun getFusedLocationProviderClient(context: Context) =
        instantiateFusedLocationProviderClient(context)

    private fun instantiateFusedLocationProviderClient(
        context: Context
    ): FusedLocationProviderClient =
        this::class.java.classLoader
            ?.loadClass(platformDelegate.fusedLocationProviderClientClassName())
            ?.getDeclaredConstructor(Context::class.java)
            ?.newInstance(context) as FusedLocationProviderClient

    fun isPlatformServiceEnabled(applicationContext: Context): Boolean =
        platformDelegate.isPlatformServiceEnabled(applicationContext)

    fun getPlatformProvider() = platformDelegate.getPlatformProvider()

    fun getSettingsClient(activity: Activity): SettingsClient =
        platformDelegate.getSettingsClient(activity = activity)
}
