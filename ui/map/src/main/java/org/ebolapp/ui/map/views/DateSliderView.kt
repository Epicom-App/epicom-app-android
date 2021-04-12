package org.ebolapp.ui.map.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import org.ebolapp.presentation.map.MapState
import org.ebolapp.ui.common.extensions.getColor
import org.ebolapp.ui.common.extensions.getColorFromAttr
import org.ebolapp.ui.map.R
import org.ebolapp.ui.map.databinding.UiMapDaySliderBinding
import java.time.LocalDate
import java.util.*


class DateSliderView(
    context: Context,
    attrs: AttributeSet
) : CardView(context, attrs) {

    private val binding: UiMapDaySliderBinding =
        UiMapDaySliderBinding.inflate(LayoutInflater.from(context), this)

    val dateSlider = binding.dateSlider
    val datePickerButton = binding.datePickerButton
    val dateLabelFromday = binding.dateLabelFromday
    val dateLabelToday = binding.dateLabelToday

    // Todo: extract this and all hardcoded parameters to view attributes
    private val trackWidth = 10f
    private val tickHeight = 16f

    private val dangerColor = context.getColorFromAttr(R.attr.colorError)
    private var dangerValues: List<Int> = listOf()
    private val dangerCircleDrawable: Drawable
    private val dangerCircleSize = 50

    init {
        with(dateSlider) {
            datePickerButton.text = formatDateButtonLabel(
                daysAgo = valueTo - value,
                maxDays = valueTo
            )
            setLabelFormatter {
                val date = formatDateSliderLabel(
                    daysAgo = valueTo - value,
                    maxDays = valueTo
                )
                datePickerButton.text = formatDateButtonLabel(
                    daysAgo = valueTo - value,
                    maxDays = valueTo
                )
                date
            }
        }
        dateLabelFromday.text =
            resources.getString(R.string.map_slider_days_ago, dateSlider.valueTo.toInt())
        dangerCircleDrawable = ContextCompat.getDrawable(
            context,
            R.drawable.ui_map_ic_alert_circle
        )!!.apply {
            setTint(dangerColor)
        }
    }

    fun renderSliderState(sliderState: MapState.SliderState) {
        with(dateSlider) {
            value = sliderState.value.toFloat()
            valueFrom = sliderState.from.toFloat()
            valueTo = sliderState.to.toFloat()
            datePickerButton.text = formatDateButtonLabel(
                daysAgo = valueTo - value,
                maxDays = valueTo
            )
            setDangerValues(sliderState.exposedDays)
        }
    }

    private val normalPaint = Paint().also {
        it.color = getColor(R.color.colorSliderScale)
        it.strokeWidth = trackWidth
        it.strokeCap = Paint.Cap.ROUND
    }

    private val dangerPaint = Paint().also {
        it.color = dangerColor
        it.strokeWidth = trackWidth
        it.strokeCap = Paint.Cap.ROUND
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas ?: return

        with(canvas) {

            val daySliderPosition = getChildViewPositionInParent(
                parent = this@DateSliderView,
                child = dateSlider
            )

            val stepCount = (dateSlider.valueTo - dateSlider.valueFrom) / dateSlider.stepSize
            val stepWidth = (dateSlider.width - (dateSlider.trackSidePadding * 2)) / stepCount

            // Draw track
            drawLine(
                daySliderPosition.x + dateSlider.trackSidePadding,
                daySliderPosition.y + dateSlider.height / 2f,
                daySliderPosition.x - dateSlider.trackSidePadding + dateSlider.width,
                daySliderPosition.y + dateSlider.height / 2f,
                normalPaint
            )

            // Draw tick
            (0..stepCount.toInt()).forEach { index ->
                drawLine(
                    daySliderPosition.x + dateSlider.trackSidePadding + (stepWidth * index),
                    daySliderPosition.y + dateSlider.height / 2f - tickHeight,
                    daySliderPosition.x + dateSlider.trackSidePadding + (stepWidth * index),
                    daySliderPosition.y + dateSlider.height / 2f + tickHeight,
                    if (index in dangerValues) dangerPaint else normalPaint
                )
                if (index in dangerValues) {
                    val halfSize = dangerCircleSize / 2
                    val x = (daySliderPosition.x +
                                dateSlider.trackSidePadding +
                                    (stepWidth * index) -
                                        halfSize).toInt()
                    val y = (dateSlider.bottom) - halfSize - 8
                    val width = x + dangerCircleSize
                    val height = y + dangerCircleSize
                    dangerCircleDrawable.setBounds(x, y, width, height)
                    dangerCircleDrawable.draw(this)
                }
            }
        }

        super.dispatchDraw(canvas)
    }

    private fun setDangerValues(dangerValues: List<Int>) {
        this.dangerValues = dangerValues
        this.postInvalidate()
    }

    private fun getChildViewPositionInParent(parent: ViewGroup, child: View): Position {
        val relativePosition = intArrayOf(child.left, child.top)
        val positionPreview = intArrayOf(1, 2)
        val positionFrame = intArrayOf(1, 2)
        parent.getLocationInWindow(positionPreview)
        child.getLocationInWindow(positionFrame)
        relativePosition[0] = positionFrame[0] - positionPreview[0]
        relativePosition[1] = positionFrame[1] - positionPreview[1]
        return Position(relativePosition[0].toFloat(), relativePosition[1].toFloat())
    }

    private data class Position(val x: Float, val y: Float)

    private fun formatDateSliderLabel(daysAgo: Float, maxDays: Float = 14f): String {
        val date = LocalDate.now().minusDays(daysAgo.toLong())
        val dayOfTheWeek = date.month.name
            .toLowerCase(Locale.getDefault())
            .capitalize(Locale.getDefault())
            .subSequence(0, 3)
        val dayOfTheMonth = date.dayOfMonth
        return "$dayOfTheWeek $dayOfTheMonth"
    }

    private fun formatDateButtonLabel(daysAgo: Float, maxDays: Float = 14f): String {
        val date = LocalDate.now().minusDays(daysAgo.toLong())
        val dayOfTheWeek = date.month.name
            .toLowerCase(Locale.getDefault())
            .capitalize(Locale.getDefault())
            .subSequence(0, 3)
        val dayOfTheMonth = date.dayOfMonth
        val year = date.year
        return "$dayOfTheWeek $dayOfTheMonth $year"
    }
}
