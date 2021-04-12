package no.brand.tableview.entities

data class Column(
    val header: Cell = Cell("idx",value = ""),
    val cells: List<Cell> = emptyList()
)