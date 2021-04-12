package org.ebolapp.presentation.nextsteps

import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.presentation.common.toLocalDate
import java.time.LocalDate

data class RiskMatchListItem(
    val id: String,
    val locationName: String,
    val date: LocalDate,
    val durationSec: Long,
    val contactNumber: String,
)

internal fun RiskMatch.toRiskMatchListItem(
    locationName: String,
    contactNumber: String
): RiskMatchListItem = RiskMatchListItem(
    id = "${regionId}_${visitId}",
    locationName = locationName,
    date = this.dayStartTimestampSec.toLocalDate(),
    durationSec = durationSec(),
    contactNumber = contactNumber
)