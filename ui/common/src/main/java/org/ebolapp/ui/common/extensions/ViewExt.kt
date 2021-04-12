package org.ebolapp.ui.common.extensions

import android.animation.LayoutTransition
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


fun View.show() {
    if (this.visibility != View.VISIBLE)
        this.visibility = View.VISIBLE
}

fun View.hide() {
    if (this.visibility != View.INVISIBLE)
        this.visibility = View.INVISIBLE
}

fun View.gone() {
    if (this.visibility != View.GONE)
        this.visibility = View.GONE
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.isNotVisible() = !this.isVisible()

fun View.isGone() = this.visibility == View.GONE

fun View.isHidden() = this.visibility == View.INVISIBLE

fun View.showOrGone(show: Boolean, block: () -> Unit = {}) {
    if (show) {
        this.show(); block()
    } else this.gone()
}

fun View.showOrHide(show: Boolean, block: () -> Unit = {}) {
    if (show) {
        this.show(); block()
    } else this.hide()
}

fun View.getThemedColor(attribute: Int): Int {
    val typedValue = TypedValue()
    val theme = this.context.theme
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.data
}

fun View.getColor(@ColorRes colorResId: Int) : Int {
    return ContextCompat.getColor(context, colorResId)
}

fun Checkable.checked(state: Boolean) {
    if (this.isChecked != state) this.isChecked = state
}

fun View.goneNoAnim() {
    if (this.visibility != View.GONE) {
        val lt = (parent as ViewGroup).layoutTransition
        lt.disableTransitionType(LayoutTransition.DISAPPEARING)
        visibility = View.GONE
        lt.enableTransitionType(LayoutTransition.DISAPPEARING)
    }
}

fun View.showNoAnim() {
    if (this.visibility != View.VISIBLE) {
        val lt = (parent as ViewGroup).layoutTransition
        lt.disableTransitionType(LayoutTransition.APPEARING)
        visibility = View.VISIBLE
        lt.enableTransitionType(LayoutTransition.APPEARING)
    }
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}