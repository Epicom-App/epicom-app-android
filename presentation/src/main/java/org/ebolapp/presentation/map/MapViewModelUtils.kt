package org.ebolapp.presentation.map


internal fun MapState.SliderState.accumulateExposedDay(
    offset: Int
): List<Int> = (exposedDays + (to - offset)).distinct()
