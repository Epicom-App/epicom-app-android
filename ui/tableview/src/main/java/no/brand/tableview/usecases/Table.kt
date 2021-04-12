package no.brand.tableview.usecases

import no.brand.tableview.adapters.TableMapper
import no.brand.tableview.entities.Cell
import no.brand.tableview.entities.Column
import no.brand.tableview.entities.Row
import no.brand.tableview.entities.Table


internal fun <T> buildTable(
    data: List<T>,
    mapper: TableMapper<T>
): Table = Table(
    header = newIndexedRow(" - ", mapper.header()),
    rows = data.mapIndexed { index, dataRow ->
        newIndexedRow(index.toString(), mapper.row(dataRow))
    }
)

private fun newIndexedRow(index: String, row: Row): Row =
    Row(cells = listOf(Cell(id = "idx", value = index)) + row.cells)

fun Table.columnsCount() = this.header.cells.count()

fun Table.totalRowsCount() = this.rows.count() + 1

fun Table.columnBy(index: Int): Column = Column(
    header = this.header.cells[index],
    cells = this.rows.map { it.cells[index] }
)

fun Table.absRowBy(absIndex: Int): Row {
    return if (absIndex == 0) header else rows[absIndex - 1]
}

fun Table.rowBy(index: Int): Row {
    return rows[index]
}

fun Table.widestCellInColumnBy(index: Int): Cell {
    val column = columnBy(index)
    return (column.cells + column.header).maxByOrNull { it.value.length } ?: Cell(id="",value = "")
}
