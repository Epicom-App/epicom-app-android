package org.ebolapp.ui.map.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import org.ebolapp.features.regions.entities.MapRegionInfo
import org.ebolapp.ui.common.extensions.getColor
import org.ebolapp.ui.common.extensions.hide
import org.ebolapp.ui.common.extensions.matchColor
import org.ebolapp.ui.common.extensions.show
import org.ebolapp.ui.map.R
import org.ebolapp.ui.map.databinding.UiMapAreaLegendViewBinding

class RegionInfoView(
    context: Context,
    attrs: AttributeSet
) : CardView(context, attrs) {

    private val binding: UiMapAreaLegendViewBinding =
        UiMapAreaLegendViewBinding.inflate(LayoutInflater.from(context), this)

    fun renderContent(regionInfo: MapRegionInfo,
                      onButtonClick: (String) -> Unit,
                      onSeverityClick: () -> Unit) = with(binding) {
        areaName.text = regionInfo.name
        diseaseName.text = regionInfo.disease
        casesNumber.text = regionInfo.casesNumber.toInt().toString()
        casesNumberInfo.text = regionInfo.severityInfo
        val severityBgColor = Color.parseColor(regionInfo.color)
        val severityTextColorResource = severityBgColor.matchColor(onLightColor = R.color.colorBlack, onDarkColor = R.color.colorWhite)
        val severityTextColor = getColor(severityTextColorResource)
        severity.backgroundTintList = ColorStateList.valueOf(severityBgColor)
        severityLevel.text = regionInfo.severity.toString()
        severityLevel.setTextColor(severityTextColor)
        severityMaxLevel.text = regionInfo.maxSeverity.let { "/$it" }
        severityMaxLevel.setTextColor(severityTextColor)
        severityLevelInfo.text = regionInfo.casesNumberInfo
        severityLevelLabel.setTextColor(severityTextColor)
        infoButton.setOnClickListener {
            regionInfo.informationUrl?.let { infoLink -> onButtonClick(infoLink) }
        }
        severity.setOnClickListener {
            onSeverityClick()
        }
        if (regionInfo.informationUrl == null) infoButton.hide() else infoButton.show()
    }
}

