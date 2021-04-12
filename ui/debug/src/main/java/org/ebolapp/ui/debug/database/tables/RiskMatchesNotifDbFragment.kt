package org.ebolapp.ui.debug.database.tables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import no.brand.tableview.views.FilterDialog
import org.ebolapp.presentation.debug.database.RiskMatchNotifyDbViewModel
import org.ebolapp.ui.debug.R
import org.ebolapp.ui.debug.databinding.UiDebugDbTableviewFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RiskMatchesNotifDbFragment : Fragment(R.layout.ui_debug_db_tableview_fragment) {

    private var binding: UiDebugDbTableviewFragmentBinding? = null
    private val viewModel by viewModel<RiskMatchNotifyDbViewModel>()

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