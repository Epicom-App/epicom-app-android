package org.ebolapp.ui.onboarding

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewbinding.ViewBinding

class OnboardingView<T: ViewBinding> @JvmOverloads constructor(
viewBindingFactory: (View) -> T,
context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val binding = viewBindingFactory(this)
}