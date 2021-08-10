package com.proxy.location.huawei

import com.proxy.location.LocationRequest
import com.huawei.hms.location.LocationRequest as HuaweiLocationRequest

internal fun LocationRequest.toHuaweiLocationRequest(): HuaweiLocationRequest {
    val locationRequest = this
    return HuaweiLocationRequest().apply {
        setExpirationDuration(expirationTime)
        priority = locationRequest.priority
        interval = locationRequest.interval
        maxWaitTime = locationRequest.maxWaitTime
        fastestInterval = locationRequest.fastestInterval
        expirationTime = locationRequest.expirationTime
        setNumUpdates = locationRequest.setNumUpdates
        smallestDisplacement = locationRequest.smallestDisplacement
    }
}
