package org.ebolapp.ui.debug.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.ebolapp.logging.Logging
import org.ebolapp.ui.debug.R
import org.ebolapp.ui.debug.databinding.UiDebugDbFragmentBinding
import reactivecircus.flowbinding.android.view.clicks

class DbFragment : Fragment(), Logging {

    private var binding: UiDebugDbFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UiDebugDbFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.mapRegionCases?.clicks()?.onEach {
            findNavController().navigate(R.id.casesDbDest)
        }?.launchIn(lifecycleScope)
        binding?.mapRegionLegend?.clicks()?.onEach {
            findNavController().navigate(R.id.legendDbDest)
        }?.launchIn(lifecycleScope)
        binding?.userLocations?.clicks()?.onEach {
            findNavController().navigate(R.id.locationsDbDest)
        }?.launchIn(lifecycleScope)
        binding?.visits?.clicks()?.onEach {
            findNavController().navigate(R.id.visitsDbDest)
        }?.launchIn(lifecycleScope)
        binding?.riskMatches?.clicks()?.onEach {
            findNavController().navigate(R.id.riskMatchesDbDest)
        }?.launchIn(lifecycleScope)
        binding?.riskMatchesNotified?.clicks()?.onEach {
            findNavController().navigate(R.id.riskMatchesNotifDest)
        }?.launchIn(lifecycleScope)
    }

}