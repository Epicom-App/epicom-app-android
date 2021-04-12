package no.brand.tableview.adapters

import no.brand.tableview.entities.Row

interface TableMapper<T> {
    fun header() : Row
    fun row(row:T) : Row
}