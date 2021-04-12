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
import org.ebolapp.db.MapRegionCaseLegendTable
import org.ebolapp.db.MapRegionCaseTable

import org.ebolapp.features.debug.usecases.GetMapRegionLegendDbgInfoUseCase
import org.ebolapp.presentation.common.format

class LegendDbViewModel(
    private val getMapRegionLegendDbgInfoUseCase: GetMapRegionLegendDbgInfoUseCase
) : ViewModel() {

    private val mapper = object : TableMapper<MapRegionCaseLegendTable> {
        override fun header(): Row {
            return Row(cells = listOf(
                Cell(id = "name", value = "name"),
                Cell(id = "itemId", value = "itemId"),
                Cell(id = "severity", value = "severity"),
                Cell(id = "info", value = "info"),
                Cell(id = "color", value = "color"),
                Cell(id = "isRisky", value = "isRisky")
            ))
        }
        override fun row(row: MapRegionCaseLegendTable): Row {
            return Row(cells = listOf(
                Cell(id = "name", value = row.name),
                Cell(id = "itemId", value = row.itemId.toString()),
                Cell(id = "severity", value = row.severity.toString()),
                Cell(id = "info", value = row.info ?: "-"),
                Cell(id = "color", value = row.color ?: "-"),
                Cell(id = "isRisky", value = if (row.isRisky == 0L) "false" else "true")
            ))
        }
    }

    private val _tableViewModel = TableViewModel(emptyList(), mapper)
    val tableViewModel: TableViewModel<MapRegionCaseLegendTable> get() = _tableViewModel

    init {
        viewModelScope.launch {
            _tableViewModel.updateViewModel(getMapRegionLegendDbgInfoUseCase())
        }
    }

}