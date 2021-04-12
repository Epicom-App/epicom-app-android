package no.brand.tableview.adapters

import no.brand.tableview.entities.Cell
import no.brand.tableview.entities.CellFilter
import no.brand.tableview.entities.CellSort


data class TableFilters(
        val cellSorting: Map<Cell, CellSort> = mutableMapOf(),
        val cellFilters: Map<Cell, CellFilter> = mutableMapOf(),
        val cellFiltersEdit: Map<Cell, CellFilter> = mutableMapOf()
) {

    fun containsFilter(cell: Cell) {

    }
}