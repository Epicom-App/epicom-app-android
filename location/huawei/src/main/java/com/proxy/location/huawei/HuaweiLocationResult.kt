package com.proxy.location.huawei

import android.location.Location
import com.proxy.location.LocationResult
import com.huawei.hms.location.LocationResult as HuaweiLocationResult

internal fun HuaweiLocationResult.toLocationResult() = object : LocationResult {
    override val lastLocation: Location
        get() = this@toLocationResult.lastLocation

    override fun getLocations() = this@toLocationResult.locations
}
