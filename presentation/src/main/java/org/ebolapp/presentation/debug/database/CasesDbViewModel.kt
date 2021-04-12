package org.ebolapp.presentation.debug.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.brand.tableview.adapters.TableMapper
import no.brand.tableview.adapters.TableViewModel
import no.brand.tableview.entities.Cell
import no.brand.tableview.entities.Row
import org.ebolapp.db.MapRegionCaseTable
import org.ebolapp.features.debug.usecases.GetMapRegionCasesDbgInfoUseCase
import org.ebolapp.presentation.common.format

class CasesDbViewModel(
    private val getMapRegionCasesDbgInfoUseCase: GetMapRegionCasesDbgInfoUseCase
): ViewModel() {

    private val mapper = object : TableMapper<MapRegionCaseTable> {
        override fun header(): Row {
            return Row(cells = listOf(
                Cell(id = "areaId", value = "areaId"),
                Cell(id = "severity", value = "severity"),
                Cell(id = "numberOfCases", value = "numberOfCases"),
                Cell(id = "timestampSec", value = "timestampSec")
            ))
        }
        override fun row(row: MapRegionCaseTable): Row {
            return Row(cells = listOf(
                Cell(id = "areaId", value = row.areaId),
                Cell(id = "severity", value = row.severity.toString()),
                Cell(id = "numberOfCases", value = row.numberOfCases.toString()),
                Cell(id = "timestampSec", value = row.timestsampSec.format())
            ))
        }
    }

    private val _tableViewModel = TableViewModel(emptyList(), mapper)
    val tableViewModel: TableViewModel<MapRegionCaseTable> get() = _tableViewModel

    init {
        viewModelScope.launch {
            _tableViewModel.updateViewModel(getMapRegionCasesDbgInfoUseCase())
        }
    }
}

