package org.ebolapp.ui.map.views

import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.ebolapp.ui.map.MapFragment
import org.ebolapp.ui.map.R
import org.koin.core.component.KoinApiExtension
import reactivecircus.flowbinding.material.positiveButtonClicks
import java.time.Instant
import java.util.concurrent.TimeUnit


@OptIn(
    FlowPreview::class,
    KoinApiExtension::class,
    ExperimentalCoroutinesApi::class
)
fun MapFragment.showDatePickerDialog(
    selectedTimestampSec: Long,
    onSelection: (timestampMillis: Long) -> Unit
) {
    with(
        MaterialDatePicker.Builder
            .datePicker()
            .setCalendarConstraints(createCalendarConstraints())
            .setSelection(selectedTimestampSec * 1000)
            .build()
    ) {
        positiveButtonClicks()
            .onEach { timestampMillis ->
                onSelection(timestampMillis)
            }.launchIn(lifecycleScope)
        show(this@showDatePickerDialog.childFragmentManager, "UiMapDatePicker")
    }
}

private fun createCalendarConstraints(): CalendarConstraints {
    return CalendarConstraints.Builder().setValidator(
        CompositeDateValidator.allOf(listOf(
            DateValidatorPointForward.from(
        Instant.now().toEpochMilli() - TimeUnit.DAYS.toMillis(15)
            ),
            DateValidatorPointBackward.before(Instant.now().toEpochMilli())
        ))
    ).build()
}