package no.brand.tableview.recycler

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import no.brand.tableview.R
import no.brand.tableview.adapters.TableViewModel
import no.brand.tableview.entities.Table
import no.brand.tableview.usecases.rowBy
import no.brand.tableview.usecases.totalRowsCount
import no.brand.tableview.usecases.widestCellInColumnBy
import kotlin.coroutines.CoroutineContext

class TableIndexViewAdapter<T>(
        private val viewModel: TableViewModel<T>
) : RecyclerView.Adapter<TableIndexViewAdapter.IndexRowViewHolder>(), CoroutineScope {

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
                notifyDataSetChanged()
            }
        }
    }


    class IndexRowViewHolder(
            val view: ViewGroup
    ) : RecyclerView.ViewHolder(view) {
        val value: TextView
        init {
            value = view.findViewById(R.id.value)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexRowViewHolder {
        return IndexRowViewHolder(
                parent.newTableCell().apply {
                    updateLayoutParams {
                        width = cellWidthInPx(table.widestCellInColumnBy(0))
                    }
                }
        )
    }

    override fun onBindViewHolder(rowView: IndexRowViewHolder, position: Int) {
        rowView.view.findViewById<View>(R.id.start).visible()
        rowView.value.text = table.rowBy(position).cells[0].value
        rowView.view.apply {
            updateLayoutParams {
                width = cellWidthInPx(table.widestCellInColumnBy(0))
            }
        }
    }

    override fun getItemCount(): Int = table.totalRowsCount() - 1

}