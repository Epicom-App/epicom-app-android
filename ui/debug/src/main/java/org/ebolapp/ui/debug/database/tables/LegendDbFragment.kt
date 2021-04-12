package org.ebolapp.ui.debug.database.tables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import no.brand.tableview.views.FilterDialog
import org.ebolapp.presentation.debug.database.LegendDbViewModel
import org.ebolapp.ui.common.extensions.observe
import org.ebolapp.ui.debug.R
import org.ebolapp.ui.debug.databinding.UiDebugDbTableFragmentBinding
import org.ebolapp.ui.debug.databinding.UiDebugDbTableviewFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LegendDbFragment: Fragment(R.layout.ui_debug_db_tableview_fragment) {

    private var binding: UiDebugDbTableviewFragmentBinding? = null
    private val viewModel by viewModel<LegendDbViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UiDebugDbTableviewFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tableView?.setTableViewModel(viewModel.tableViewModel)
        lifecycleScope.launchWhenResumed {
            viewModel.tableViewModel.tableFiltersState.collectLatest {
                FilterDialog.show(childFragmentManager, viewModel.tableViewModel)
            }
        }
    }

}