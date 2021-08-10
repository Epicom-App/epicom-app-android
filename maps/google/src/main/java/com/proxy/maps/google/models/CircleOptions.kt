package com.proxy.maps.google.models

import com.proxy.maps.model.CircleOptions
import com.proxy.maps.model.PatternItem
import com.google.android.gms.maps.model.CircleOptions as GoogleCircleOptions

internal fun CircleOptions.toGoogleCircleOptions(): GoogleCircleOptions = GoogleCircleOptions()
    .center(center.toGoogleLatLng())
    .fillColor(fillColor)
    .radius(radius)
    .strokeColor(strokeColor)
    .strokePattern(strokePattern?.map(PatternItem::toGooglePatternItem))
    .strokeWidth(strokeWidth)
    .zIndex(zIndex)
    .clickable(clickable)
    .visible(visible)
