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
import org.ebolapp.db.UserLocationTable
import org.ebolapp.db.VisitTable

import org.ebolapp.features.debug.usecases.GetVisitsDbgInfoUseCase
import org.ebolapp.presentation.common.format

class VisitsDbViewModel(
    private val getVisitsDbgInfoUseCase: GetVisitsDbgInfoUseCase
) : ViewModel() {

    private val mapper = object : TableMapper<VisitTable> {
        override fun header(): Row {
            return Row(cells = listOf(
                Cell(id = "id", value = "id"),
                Cell(id = "lat", value = "lat"),
                Cell(id = "lng", value = "lng"),
                Cell(id = "startTimestampSec", value = "startTimestampSec"),
                Cell(id = "endTimestampSec", value = "endTimestampSec")
            ))
        }
        override fun row(row: VisitTable): Row {
            return Row(cells = listOf(
                Cell(id = "id", value = row.id.toString()),
                Cell(id = "lat", value = row.lat.toString()),
                Cell(id = "lng", value = row.lon.toString()),
                Cell(id = "startTimestampSec", value = row.startTimestampSec.format()),
                Cell(id = "endTimestampSec", value = row.endTimestampSec.format())
            ))
        }
    }

    private val _tableViewModel = TableViewModel(emptyList(), mapper)
    val tableViewModel: TableViewModel<VisitTable> get() = _tableViewModel

    init {
        viewModelScope.launch {
            _tableViewModel.updateViewModel(getVisitsDbgInfoUseCase())
        }
    }
}