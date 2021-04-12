package no.brand.tableview.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import no.brand.tableview.R
import no.brand.tableview.entities.Cell

internal fun ViewGroup.newTableRow(): LinearLayout = LinearLayout(context).apply {
    orientation = LinearLayout.HORIZONTAL
    layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
    )
}

internal fun LinearLayout.newTableCell(): View = LayoutInflater
        .from(this.context)
        .inflate(R.layout.table_cell, this, false)

internal fun ViewGroup.newTableCell(): ViewGroup = LayoutInflater
        .from(this.context)
        .inflate(R.layout.table_cell, this, false) as ViewGroup

internal fun ViewGroup.headerCellView(
        index: Int
): TextView {
    return if (index == 0)
        this.children.first().apply {
            findViewById<View>(R.id.top).visible()
            findViewById<View>(R.id.start).visible()
        }.findViewById(R.id.value)
    else
        this[index].apply {
            findViewById<View>(R.id.top).visible()
        }.findViewById(R.id.value)
}

internal fun TableViewAdapter.RowViewHolder.cellView(
        index: Int
): TextView {
    return view[index].findViewById(R.id.value)
}

internal fun View.visible() {
    if (!isVisible) visibility = View.VISIBLE
}

internal fun View.gone() {
    if (!isGone) visibility = View.GONE
}


internal fun ViewGroup.cellWidthInPx(cell: Cell, hrzPadding: Int = 30): Int {
    val paint = TextView(this.context).paint
    val cellTextMeasurement: Float = paint?.measureText(cell.value) ?: 100f
    return cellTextMeasurement.toInt() + hrzPadding * 2
}