package no.brand.tableview.entities

data class Table(
    val header: Row = Row(cells = listOf(Cell(id = "idx","-"))),
    val rows: List<Row> = emptyList()
)