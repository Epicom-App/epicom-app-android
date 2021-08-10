package com.proxy.location.huawei

import android.content.Context
import androidx.annotation.Keep
import com.proxy.location.LocationServicesDelegates
import com.proxy.location.PlatformProvider

@Keep
internal class HuaweiLocationServices : LocationServicesDelegates {
    override fun fusedLocationProviderClientClassName(): String =
        HuaweiFusedLocationProviderClient::class.java.name

    /**
     *   Huawei updates it's services on the spot, prompting user
     */
    override fun isPlatformServiceEnabled(applicationContext: Context): Boolean = true
    override fun getPlatformProvider() = PlatformProvider.Huawei
}
