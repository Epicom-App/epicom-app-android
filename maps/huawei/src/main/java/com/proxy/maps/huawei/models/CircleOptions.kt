package com.proxy.maps.huawei.models

import com.proxy.maps.model.CircleOptions
import com.proxy.maps.model.PatternItem
import com.huawei.hms.maps.model.CircleOptions as HuaweiCircleOptions

internal fun CircleOptions.toHuaweiCircleOptions(): HuaweiCircleOptions = HuaweiCircleOptions()
    .center(center.toHuaweiLatLng())
    .fillColor(fillColor)
    .radius(radius)
    .strokeColor(strokeColor)
    .strokePattern(strokePattern?.map(PatternItem::toHuaweiPatternItem))
    .strokeWidth(strokeWidth)
    .zIndex(zIndex)
    .clickable(clickable)
    .visible(visible)
