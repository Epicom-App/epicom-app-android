package org.ebolapp.ui.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.ebolapp.ui.debug.databinding.UiDebugFragmentBinding
import reactivecircus.flowbinding.android.view.clicks

class DebugFragment : Fragment(R.layout.ui_debug_fragment) {

    private var binding: UiDebugFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UiDebugFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.appsLogs?.clicks()?.onEach {
            findNavController().navigate(R.id.logsDest)
        }?.launchIn(lifecycleScope)
        binding?.backgroundJobsLogs?.clicks()?.onEach {
            findNavController().navigate(R.id.jobLogsDest)
        }?.launchIn(lifecycleScope)
        binding?.database?.clicks()?.onEach {
            findNavController().navigate(R.id.databaseDest)
        }?.launchIn(lifecycleScope)
    }

}