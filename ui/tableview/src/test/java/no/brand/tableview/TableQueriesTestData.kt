package no.brand.tableview

import no.brand.tableview.adapters.TableMapper
import no.brand.tableview.entities.Cell
import no.brand.tableview.entities.Row


data class Data(
    val id: String,
    val name: String,
    val count: String,
    val date: String,
    val selected: String
)

val data = listOf(
    Data(id="1",name = "name1",count = "1123312337412413213123123123123123123123123123",date = "22.33",selected = "false"),
    Data(id="2",name = "name2",count = "31231231231231231231231232",date = "22.33",selected = "true"),
    Data(id="3",name = "name3",count = "31231231233",date = "22.33",selected = "false"),
    Data(id="4",name = "name4",count = "431233712332131",date = "22.33",selected = "true"),
    Data(id="5",name = "name5",count = "531231321",date = "22.33",selected = "false"),
    Data(id="6",name = "name6",count = "63123123332",date = "22.33",selected = "false"),
    Data(id="7",name = "name7",count = "312323231323337",date = "22.33",selected = "false"),
)

val mapper = object : TableMapper<Data> {
    override fun header(): Row {
        return Row(cells = listOf(
            Cell(id = "id", value = "id"),
            Cell(id = "name", value = "name"),
            Cell(id = "count", value = "count"),
            Cell(id = "date", value = "date"),
            Cell(id = "selected", value = "selected")
        ))
    }
    override fun row(row: Data): Row {
        return Row(cells = listOf(
            Cell(id = "id", value = row.id),
            Cell(id = "name", value = row.name),
            Cell(id = "count", value = row.count),
            Cell(id = "date", value = row.date),
            Cell(id = "selected", value = row.selected)
        ))
    }
}