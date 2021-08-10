package com.proxy.maps.google.models

import com.proxy.maps.model.Dash
import com.proxy.maps.model.Dot
import com.proxy.maps.model.Gap
import com.proxy.maps.model.PatternItem
import com.google.android.gms.maps.model.Dash as GoogleDash
import com.google.android.gms.maps.model.Dot as GoogleDot
import com.google.android.gms.maps.model.Gap as GoogleGap
import com.google.android.gms.maps.model.PatternItem as GooglePatternItem

internal fun PatternItem.toGooglePatternItem(): GooglePatternItem =
    when (this) {
        is Dash -> GoogleDash(length)
        is Gap -> GoogleGap(length)
        is Dot -> GoogleDot()
    }

internal fun GooglePatternItem.toPatternItem(): PatternItem =
    when (this) {
        is GoogleDash -> Dash(length = this.length)
        is GoogleGap -> Gap(length = this.length)
        is GoogleDot -> Dot
        else -> throw IllegalArgumentException("Unknown Google PatternItem type $this")
    }
