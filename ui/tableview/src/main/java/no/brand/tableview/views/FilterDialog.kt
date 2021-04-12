package no.brand.tableview.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import no.brand.tableview.R
import no.brand.tableview.adapters.TableViewModel
import no.brand.tableview.entities.Cell
import no.brand.tableview.entities.CellFilter


class FilterDialog(
        val filter: Pair<Cell, CellFilter>,
        val onOk: (filter: Pair<Cell, CellFilter>) -> Unit,
        val onCancel: () -> Unit
) : DialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = with(inflater.inflate(R.layout.table_filter_dialog, container, false)) {
        this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.title)
        val edit = view.findViewById<TextInputEditText>(R.id.filter)
        val ok = view.findViewById<MaterialButton>(R.id.ok)
        val cancel = view.findViewById<MaterialButton>(R.id.cancel)
        title.text = "Filter by ${filter.first.value} column"
        edit.setText(filter.second.filter)
        ok.setOnClickListener {
            val (cell, _) = filter
            onOk(cell to CellFilter(edit.text.toString()))
            dismiss()
        }
        cancel.setOnClickListener {
            onCancel()
            dismiss()
        }
    }

    companion object {
        fun <T> show(
                fragmentManager: FragmentManager,
                tableViewModel: TableViewModel<T>
        ) {
            if (tableViewModel.tableFiltersState.value.cellFiltersEdit.isNotEmpty()) {
                val filter = tableViewModel.tableFiltersState.value.cellFiltersEdit.asSequence().first()
                FilterDialog(
                        filter = filter.toPair(),
                        onOk = { newFilter -> tableViewModel.filterByColumnApply(newFilter) },
                        onCancel = { tableViewModel.filterByColumnCancel() }
                ).show(fragmentManager, "editFilter")
            } else {
                if (fragmentManager.findFragmentByTag("editFilter")?.isAdded == true) {
                    fragmentManager.popBackStackImmediate()
                }
            }
        }
    }
}