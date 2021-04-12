package no.brand.tableview.adapters

import kotlinx.coroutines.flow.*
import no.brand.tableview.entities.*
import no.brand.tableview.usecases.buildTable
import no.brand.tableview.usecases.columnBy

class TableViewModel<T>(
        data: List<T>,
        private val mapper: TableMapper<T>
) {

    private val _tableState: MutableStateFlow<Table> = MutableStateFlow(
        buildTable(data, mapper)
    )
    private val tableState: StateFlow<Table> get() = _tableState

    private val _tableViewState: MutableStateFlow<Table> = MutableStateFlow(tableState.value)
    val tableViewState: StateFlow<Table> get() = _tableViewState

    private val _tableFiltersState: MutableStateFlow<TableFilters> = MutableStateFlow(TableFilters())
    val tableFiltersState: StateFlow<TableFilters> get() = _tableFiltersState

    fun updateViewModel(data: List<T>) {
        _tableState.value = buildTable(data, mapper = mapper)
        applyChanges()
    }

    fun filterByColumn(columnIndex: Int) {
        val column = tableState.value.columnBy(columnIndex)
        val columnFilter = _tableFiltersState.value.cellFilters[column.header] ?: CellFilter()
        _tableFiltersState.value = _tableFiltersState.value.copy(
                cellFiltersEdit = mapOf(column.header to columnFilter)
        )
    }

    fun filterByColumnApply(cellFilter: Pair<Cell, CellFilter>) {
        val cellFilters: MutableMap<Cell, CellFilter> = _tableFiltersState.value.cellFilters.toMutableMap()

        val (columnCell, columnCellFilter) = cellFilter
        val newCellFilters: Map<Cell, CellFilter> = if (columnCellFilter.isSet()) {
            cellFilters[columnCell] = columnCellFilter
            cellFilters
        } else {
            cellFilters.remove(columnCell)
            cellFilters
        }

        _tableFiltersState.value = _tableFiltersState.value.copy(
                cellFilters = newCellFilters,
                cellFiltersEdit = mapOf()
        )
        applyChanges()
    }

    fun filterByColumnCancel() {
        _tableFiltersState.value = _tableFiltersState.value.copy(
                cellFiltersEdit = mapOf()
        )
    }

    fun sortByColumn(columnIndex: Int) {
        val column = tableState.value.columnBy(columnIndex)
        val columnSorting = _tableFiltersState.value.cellSorting[column.header] ?: CellSort.ASC
        val newColumnSorting = when (columnSorting) {
            CellSort.NONE -> CellSort.ASC
            CellSort.ASC -> CellSort.DESC
            CellSort.DESC -> CellSort.ASC
        }
        val cellSorting = mapOf(column.header to newColumnSorting)
        _tableFiltersState.value = _tableFiltersState.value.copy(
                cellSorting = cellSorting
        )
        applyChanges()
    }

    private fun applyChanges() {
        _tableViewState.value = tableState.value.copy(
                rows = tableState.value.rows.applyFilters().applySorting().applyIndex()
        )
    }

    private fun List<Row>.applyFilters(): List<Row> {
        val cellFilters = _tableFiltersState.value.cellFilters
        if (cellFilters.isEmpty()) return this
        return this.filter row@{ row ->
            row.cells.filter cell@{ cell ->
                val cellFilterKey = cellFilters.keys.find { it.id == cell.id } ?: return@cell false
                val cellFilter = cellFilters[cellFilterKey] ?: return@cell false
                cell.value.contains(cellFilter.filter)
            }.let { it.count() == cellFilters.count() }
        }
    }

    private fun List<Row>.applySorting(): List<Row> {
        if (_tableFiltersState.value.cellSorting.keys.isEmpty()) return this
        val cellKey = _tableFiltersState.value.cellSorting.keys.first()
        val cellSort = _tableFiltersState.value.cellSorting[cellKey] ?: CellSort.ASC
        return when (cellSort) {
            CellSort.ASC -> this.sortedBy { row ->
                val index = row.cells.indexOfFirst { cell -> cell.id == cellKey.id }
                row.cells[index].value
            }
            CellSort.DESC -> this.sortedByDescending { row ->
                val index = row.cells.indexOfFirst { cell -> cell.id == cellKey.id }
                row.cells[index].value
            }
            else -> this
        }
    }

    private fun List<Row>.applyIndex(): List<Row> {
        return this.mapIndexed { index, row ->
            row.copy(cells = (listOf(row.cells[0].copy(value = index.toString())) + row.cells.subList(1, row.cells.size)))
        }
    }

    fun copyCellValue(rowIndex: Int, colIndex: Int) {
        // Todo: Implement coppy
    }
}