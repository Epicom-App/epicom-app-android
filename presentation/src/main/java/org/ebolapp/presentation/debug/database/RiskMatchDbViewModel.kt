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
import org.ebolapp.db.RiskMatchTable
import org.ebolapp.db.VisitTable
import org.ebolapp.features.debug.usecases.GetRiskMatchDbgInfoUseCase
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.presentation.common.format

class RiskMatchDbViewModel(
    private val getRiskMatchDbgInfoUseCase: GetRiskMatchDbgInfoUseCase
) : ViewModel() {

    private val mapper = object : TableMapper<RiskMatchTable> {
        override fun header(): Row {
            return Row(cells = listOf(
                Cell(id = "regionId", value = "regionId"),
                Cell(id = "visitId", value = "visitId"),
                Cell(id = "dayStartTimestampSec", value = "dayStartTimestampSec"),
                Cell(id = "visit", value = "visit"),
                Cell(id = "severity", value = "severity")
            ))
        }
        override fun row(row: RiskMatchTable): Row {
            return Row(cells = listOf(
                Cell(id = "regionId", value = row.regionId),
                Cell(id = "visitId", value = row.visitId.toString()),
                Cell(id = "dayStartTimestampSec", value = row.dayStartTimestampSec.format()),
                Cell(id = "visit", value = row.visit.toString()),
                Cell(id = "severity", value = row.severity.toString())
            ))
        }
    }

    private val _tableViewModel = TableViewModel(emptyList(), mapper)
    val tableViewModel: TableViewModel<RiskMatchTable> get() = _tableViewModel

    init {
        viewModelScope.launch {
            _tableViewModel.updateViewModel(getRiskMatchDbgInfoUseCase())
        }
    }
}