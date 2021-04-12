package org.ebolapp.ui.common.extensions

import android.graphics.Color

// W3C Formula: http://www.w3.org/WAI/ER/WD-AERT/#color-contrast
fun Int.isLightColor(): Boolean {
    val lightness: Double = (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
    return lightness > 0.5
}

fun Int.matchColor(onLightColor: Int, onDarkColor: Int) : Int = if (isLightColor()) onLightColor else onDarkColor