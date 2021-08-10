package com.proxy.location.huawei

import com.proxy.location.LocationAvailability
import com.huawei.hms.location.LocationAvailability as HuaweiLocationAvailability

internal fun HuaweiLocationAvailability.toLocationAvailability() = object : LocationAvailability {
    override fun isLocationAvailable() = this@toLocationAvailability.isLocationAvailable
}
