package com.proxy.location

interface LocationSettingsRequest {
    val locationRequests: List<LocationRequest>
    interface Builder {
        fun build()
        fun setAlwaysShow(show: Boolean)
        fun addLocationRequest()
    }
}
