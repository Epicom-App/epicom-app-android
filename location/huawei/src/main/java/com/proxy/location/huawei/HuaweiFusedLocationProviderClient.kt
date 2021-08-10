package com.proxy.location.huawei

import android.app.PendingIntent
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.Keep
import androidx.annotation.RequiresPermission
import com.huawei.hms.location.LocationServices
import com.proxy.location.CancellationToken
import com.proxy.location.FusedLocationProviderClient
import com.proxy.location.LocationAvailability
import com.proxy.location.LocationCallback
import com.proxy.location.LocationRequest
import com.proxy.location.Task
import com.huawei.hms.location.FusedLocationProviderClient as HuaweiClient

@Keep
internal class HuaweiFusedLocationProviderClient(
    context: Context
) : FusedLocationProviderClient {

    private val huaweiClient: HuaweiClient =
        LocationServices.getFusedLocationProviderClient(context)

    override val lastLocation: Task<Location?>
        @RequiresPermission(
            anyOf = [
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION"
            ]
        )
        get() = huaweiClient.lastLocation.toTask()

    override val looper: Looper
        get() = Looper.getMainLooper()

    // Note: this method is not used at the moment by the app,
    // Huawei does not have an analogue for this method.
    // If a need will appear at some point, can implemented with
    // usual Android location request, for the freshest location.
    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun getCurrentLocation(
        priority: Int,
        cancellationToken: CancellationToken
    ): Task<Location?> = huaweiClient.lastLocation.toTask()

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun getLocationAvailability(): Task<LocationAvailability> =
        huaweiClient.locationAvailability.toTask()

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun requestLocationUpdates(
        locationRequest: LocationRequest,
        pendingIntent: PendingIntent
    ): Task<Void> =
        huaweiClient.requestLocationUpdates(
            locationRequest.toHuaweiLocationRequest(),
            pendingIntent
        ).toTask()

    override fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback,
        looper: Looper
    ): Task<Void> =
        huaweiClient.requestLocationUpdates(
            locationRequest.toHuaweiLocationRequest(),
            locationCallback.toHuaweiLocationCallback(),
            looper
        ).toTask()

    override fun removeLocationUpdates(locationCallback: LocationCallback): Task<Void> =
        huaweiClient.removeLocationUpdates(
            locationCallback.toHuaweiLocationCallback()
        ).toTask()

    override fun removeLocationUpdates(pendingIntent: PendingIntent): Task<Void> =
        huaweiClient.removeLocationUpdates(pendingIntent).toTask()

    override fun flushLocations(): Task<Void> =
        huaweiClient.flushLocations().toTask()
}
