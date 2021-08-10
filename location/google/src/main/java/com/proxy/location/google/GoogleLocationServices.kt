package com.proxy.location.google

import android.content.Context
import androidx.annotation.Keep
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices as GLocationServices
import android.app.Activity
import com.proxy.location.LocationServicesDelegates
import com.proxy.location.PlatformProvider
import com.proxy.location.SettingsClient

@Keep
internal class GoogleLocationServices : LocationServicesDelegates {
    override fun fusedLocationProviderClientClassName(): String =
        GoogleFusedLocationProviderClient::class.java.name

    /**
     * Checks if google services are available and are up to date.
     */
    override fun isPlatformServiceEnabled(applicationContext: Context): Boolean =
        GoogleApiAvailability
            .getInstance()
            .isGooglePlayServicesAvailable(applicationContext) == ConnectionResult.SUCCESS

    override fun getPlatformProvider() = PlatformProvider.Google

    override fun getSettingsClient(activity: Activity): SettingsClient =
        GLocationServices.getSettingsClient(activity).toSettingsClient()

}
