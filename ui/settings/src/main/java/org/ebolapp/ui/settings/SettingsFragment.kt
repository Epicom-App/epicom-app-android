package org.ebolapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.ebolapp.ui.navigation.Screen
import org.ebolapp.ui.navigation.ScreenNavigator
import org.ebolapp.ui.settings.databinding.UiSettingsFragmentBinding
import org.koin.android.ext.android.inject
import reactivecircus.flowbinding.android.view.clicks

class SettingsFragment(private val screenNavigator: ScreenNavigator) : Fragment(R.layout.ui_settings_fragment) {

    private var _binding: UiSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UiSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
    }

    private fun setupActions() {
        binding.onboarding.clicks().onEach {
            screenNavigator.open(Screen.Onboarding)
        }.launchIn(lifecycleScope)
        binding.imprint.clicks().onEach {
            screenNavigator.open(Screen.Imprint)
        }.launchIn(lifecycleScope)
        binding.licenses.clicks().onEach {
            screenNavigator.open(Screen.Licenses)
        }.launchIn(lifecycleScope)
        binding.dataPrivacy.clicks().onEach {
            screenNavigator.open(Screen.DataPrivacy)
        }.launchIn(lifecycleScope)
        binding.about.clicks().onEach {
            screenNavigator.open(Screen.About)
        }.launchIn(lifecycleScope)
    }
}