package no.brand.tableview.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.brand.tableview.R
import no.brand.tableview.adapters.TableViewModel
import no.brand.tableview.recycler.*
import no.brand.tableview.usecases.columnsCount
import no.brand.tableview.usecases.widestCellInColumnBy


class TableView(
        context: Context,
        attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val indexHeader: LinearLayout
    private val index: RecyclerView
    private val contentHeader: LinearLayout
    private val content: RecyclerView

    init {
        inflate(getContext(), R.layout.table_view, this)
        indexHeader = findViewById(R.id.zTableIndexHeaderView)
        index = findViewById(R.id.zTableIndexView)
        contentHeader = findViewById(R.id.zTableHeaderView)
        content = findViewById(R.id.zTableView)
    }

    fun <T> setTableViewModel(tableViewModel: TableViewModel<T>) {
        setupTableIndex(tableViewModel)
        setupTable(tableViewModel)
        syncIndexWithContentScroll(index, content)
    }

    private fun <T> setupTableIndex(tableViewModel: TableViewModel<T>) {
        index.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        index.adapter = TableIndexViewAdapter(tableViewModel)
    }

    private fun <T> setupTable(tableViewModel: TableViewModel<T>) {
        content.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        content.adapter = TableViewAdapter(tableViewModel) { table ->
            indexHeader.removeAllViews()
            indexHeader.apply linearLayout@{
                newTableCell().apply {
                    updateLayoutParams {
                        width = cellWidthInPx(table.widestCellInColumnBy(0))
                    }
                    this@linearLayout.addView(this)
                    headerCellView(0).text = table.header.cells[0].value
                }

            }
            contentHeader.removeAllViews()
            contentHeader.apply linearLayout@{
                for (index in 1 until table.columnsCount()) {
                    newTableCell().apply {
                        updateLayoutParams {
                            width = cellWidthInPx(table.widestCellInColumnBy(index))
                        }
                        this.findViewById<View>(R.id.top).visible()
                        this.findViewById<TextView>(R.id.value).text = table.header.cells[index].value
                        this.setOnClickListener {
                            tableViewModel.sortByColumn(index)
                        }
                        this.setOnLongClickListener {
                            tableViewModel.filterByColumn(index)
                            true
                        }
                        this@linearLayout.addView(this)
                    }
                }
            }
        }
    }

    private fun syncIndexWithContentScroll(index: RecyclerView, content: RecyclerView) {
        index.addOnItemTouchListener(RecyclerViewUserScrollDisabler())
        content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                index.scrollBy(0, dy)
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

}