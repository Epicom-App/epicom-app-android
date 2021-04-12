package org.ebolapp.presentation.debug.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.brand.tableview.adapters.TableMapper
import no.brand.tableview.adapters.TableViewModel
import no.brand.tableview.entities.Cell
import no.brand.tableview.entities.Row
import org.ebolapp.db.UserLocationTable
import org.ebolapp.features.debug.usecases.GetUserLocationsDbgInfoUseCase
import org.ebolapp.presentation.common.format

class LocationsDbViewModel(
    private val getUserLocationsDbgInfoUseCase: GetUserLocationsDbgInfoUseCase
) : ViewModel() {

    private val mapper = object : TableMapper<UserLocationTable> {
        override fun header(): Row {
            return Row(cells = listOf(
                Cell(id = "lat", value = "lat"),
                Cell(id = "lng", value = "lng"),
                Cell(id = "timestampSec", value = "timestampSec")
            ))
        }
        override fun row(row: UserLocationTable): Row {
            return Row(cells = listOf(
                Cell(id = "lat", value = row.lat.toString()),
                Cell(id = "lng", value = row.lon.toString()),
                Cell(id = "timestampSec", value = row.timestampSec.format())
            ))
        }
    }

    private val _tableViewModel = TableViewModel(emptyList(), mapper)
    val tableViewModel: TableViewModel<UserLocationTable> get() = _tableViewModel

    init {
        viewModelScope.launch {
            _tableViewModel.updateViewModel(getUserLocationsDbgInfoUseCase())
        }
    }
}