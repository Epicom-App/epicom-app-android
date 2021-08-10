package com.proxy.location.huawei

import com.proxy.location.LocationCallback
import com.huawei.hms.location.LocationCallback as HuaweiLocationCallback
import com.huawei.hms.location.LocationAvailability as HuaweiLocationAvailability
import com.huawei.hms.location.LocationResult as HuaweiLocationResult

internal fun LocationCallback.toHuaweiLocationCallback(): HuaweiLocationCallback {
    val locationCallback = this
    return object : HuaweiLocationCallback() {
        override fun onLocationResult(locationResult: HuaweiLocationResult) {
            locationCallback.onLocationResult(locationResult.toLocationResult())
        }

        override fun onLocationAvailability(locationAvailability: HuaweiLocationAvailability?) {
            locationCallback.onLocationAvailability(locationAvailability?.toLocationAvailability())
        }
    }
}
