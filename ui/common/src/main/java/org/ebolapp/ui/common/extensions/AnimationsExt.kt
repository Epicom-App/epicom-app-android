package org.ebolapp.ui.common.extensions

import android.view.ViewGroup
import androidx.transition.Slide
import androidx.transition.TransitionManager

fun ViewGroup.applySlideTransitionAnimation(
    @Slide.GravityFlag slideDirection: Int,
    slideDuration: Long = 200L
) {
    val slide = Slide(slideDirection).apply {
        duration = slideDuration
    }
    TransitionManager.beginDelayedTransition(this, slide)
}
