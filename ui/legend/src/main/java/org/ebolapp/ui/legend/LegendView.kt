package org.ebolapp.ui.legend

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ScrollView
import org.ebolapp.ui.legend.databinding.UiLegendViewBinding

class LegendView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    attachToParent: Boolean = true
) : ScrollView(context, attrs, defStyleAttr) {

    val binding = UiLegendViewBinding.inflate(
        LayoutInflater.from(getContext()),
        this,
        attachToParent
    )

    fun setTitle(text: String) {
        binding.textViewHead.text = text
    }
}