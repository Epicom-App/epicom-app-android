package com.proxy.maps.huawei.models

import com.proxy.maps.model.Dash
import com.proxy.maps.model.Dot
import com.proxy.maps.model.Gap
import com.proxy.maps.model.PatternItem
import com.huawei.hms.maps.model.Dash as HuaweiDash
import com.huawei.hms.maps.model.Dot as HuaweiDot
import com.huawei.hms.maps.model.Gap as HuaweiGap
import com.huawei.hms.maps.model.PatternItem as HuaweiPatternItem

internal fun PatternItem.toHuaweiPatternItem(): HuaweiPatternItem =
    when (this) {
        is Dash -> HuaweiDash(length)
        is Gap -> HuaweiGap(length)
        is Dot -> HuaweiDot()
    }

internal fun HuaweiPatternItem.toPatternItem(): PatternItem =
    when (this) {
        is HuaweiDash -> Dash(length = this.length)
        is HuaweiGap -> Gap(length = this.length)
        is HuaweiDot -> Dot
        else -> throw IllegalArgumentException("Unknown Huawei PatternItem type $this")
    }
