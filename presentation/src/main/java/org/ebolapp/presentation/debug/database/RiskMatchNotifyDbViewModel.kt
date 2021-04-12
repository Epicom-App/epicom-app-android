package org.ebolapp.presentation.debug.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.brand.tableview.adapters.TableMapper
import no.brand.tableview.adapters.TableViewModel
import no.brand.tableview.entities.Cell
import no.brand.tableview.entities.Row
import org.ebolapp.db.RiskMatchNotificationTable
import org.ebolapp.db.RiskMatchTable
import org.ebolapp.features.debug.usecases.GetRiskMatchNotificationDbgInfoUseCase
import org.ebolapp.presentation.common.format

class RiskMatchNotifyDbViewModel(
    private val getRiskMatchNotificationDbgInfoUseCase: GetRiskMatchNotificationDbgInfoUseCase
) : ViewModel() {

    private val mapper = object : TableMapper<RiskMatchNotificationTable> {
        override fun header(): Row {
            return Row(cells = listOf(
                Cell(id = "regionId", value = "regionId"),
                Cell(id = "visitId", value = "visitId"),
                Cell(id = "dayStartTimestampSec", value = "dayStartTimestampSec"),
                Cell(id = "handled", value = "handled")
            ))
        }
        override fun row(row: RiskMatchNotificationTable): Row {
            return Row(cells = listOf(
                Cell(id = "regionId", value = row.regionId),
                Cell(id = "visitId", value = row.visitId.toString()),
                Cell(id = "dayStartTimestampSec", value = row.dayStartTimestampSec.format()),
                Cell(id = "handled", value = if (row.handled == 0L) "false" else "true")
            ))
        }
    }

    private val _tableViewModel = TableViewModel(emptyList(), mapper)
    val tableViewModel: TableViewModel<RiskMatchNotificationTable> get() = _tableViewModel

    init {
        viewModelScope.launch {
            _tableViewModel.updateViewModel(getRiskMatchNotificationDbgInfoUseCase())
        }
    }
}