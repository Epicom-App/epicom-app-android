package no.brand.tableview.recycler

import android.view.ViewGroup
import androidx.core.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import no.brand.tableview.adapters.TableViewModel
import no.brand.tableview.entities.Row
import no.brand.tableview.entities.Table
import no.brand.tableview.usecases.*
import kotlin.coroutines.CoroutineContext

class TableViewAdapter<T>(
        private val viewModel: TableViewModel<T>,
        private val updateHeader: (table: Table) -> Unit = {}
) : RecyclerView.Adapter<TableViewAdapter.RowViewHolder>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var table: Table = viewModel.tableViewState.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        launch {
            viewModel.tableViewState.collectLatest {
                table = it
                updateHeader(table)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = table.totalRowsCount() - 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder = createRowViewHolder(parent)

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) = renderRow(holder, table.rowBy(position))

    private fun createRowViewHolder(
            parent: ViewGroup
    ): RowViewHolder {
        val rowView = parent.newTableRow().apply linearLayout@{
            for (index in 1 until table.columnsCount()) {
                newTableCell().apply {
                    updateLayoutParams {
                        width = cellWidthInPx(table.widestCellInColumnBy(index))
                    }
                    this@linearLayout.addView(this)
                }
            }
        }
        return RowViewHolder(view = rowView, cellClick = ::cellClick)
    }

    private fun renderRow(rowView: RowViewHolder, row: Row) {
        row.cells.forEachIndexed { idx, cell ->
            if (idx != 0) rowView.cellView(idx - 1).text = cell.value
        }
        rowView.view.apply {
            children.forEachIndexed { index, view ->
                view.updateLayoutParams {
                    width = cellWidthInPx(table.widestCellInColumnBy(index+1))
                }
            }
        }
    }

    private fun cellClick(rowIndex: Int, colIndex: Int) {
        viewModel.copyCellValue(rowIndex, colIndex)
    }


    class RowViewHolder(
            val view: ViewGroup,
            val cellClick: (rowIndex: Int, columnIndex: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        init {
            val rowIndex = adapterPosition
            view.children.forEachIndexed { columnIndex, view ->
                view.setOnClickListener {
                    cellClick(rowIndex, columnIndex)
                }
            }
        }
    }
}