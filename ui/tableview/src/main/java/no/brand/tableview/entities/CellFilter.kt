package no.brand.tableview.entities

data class CellFilter(val filter: String = "") {
    fun isSet(): Boolean = filter.isNotBlank()
}