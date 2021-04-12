package org.eboalapp.ui.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import org.eboalapp.ui.main.databinding.UiMainActivityBinding
import org.ebolapp.load.regions.preferences.RegionsImportState
import org.ebolapp.logging.Logging
import org.ebolapp.presentation.main.MainViewModel
import org.ebolapp.ui.common.extensions.gone
import org.ebolapp.ui.common.extensions.observe
import org.ebolapp.ui.common.extensions.hide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), Logging {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var binding: UiMainActivityBinding

    private val permissionResultListener by inject<ActivityCompat.OnRequestPermissionsResultCallback>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.DayNight_Theme_EbolaApp)
        binding = UiMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chooseStartingScreen()
    }

    private fun chooseStartingScreen() {
        observe(viewModel.screenState) {
            when(it){
                MainViewModel.ScreenState.ONBOARDING -> setupOnboardingScreen()
                MainViewModel.ScreenState.LOADING -> setupLoadingScreen()
                MainViewModel.ScreenState.NORMAL ->  setupMainScreen()
            }
        }
    }

    private fun setupLoadingScreen() {
        binding.bottomNavigation.gone()
        findNavController(R.id.nav_host_fragment).navigate(R.id.loadingDest)
    }

    private fun setupOnboardingScreen() {
        binding.bottomNavigation.gone()
        setStartScreen(R.id.onboardingDest)
    }

    private fun setupMainScreen() {
        setStartScreen(R.id.mapDest)
        ensurePermissionsGranted()
        // The bottom navigation is temporary unavailable
        // When making visible do not forget to call
        // setupBottomNavigationView method and add 60dp bottom offset to the map
        binding.bottomNavigation.hide()
    }

    private fun setStartScreen(@IdRes screenId: Int) {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.ui_main_navigation_graph)
        graph.startDestination = screenId
        navController.graph = graph
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigation.setOnNavigationItemReselectedListener {}
        CustomBottomNavigationUi.setupWithNavController(
            binding.bottomNavigation,
            findNavController(R.id.nav_host_fragment)
        )
        observe(viewModel.newRiskMatchesCount) { newRiskMatchesCount ->
            if (newRiskMatchesCount > 0) {
                with(binding.bottomNavigation.getOrCreateBadge(R.id.nextStepsDest)) {
                    isVisible = true
                    number = newRiskMatchesCount
                }
            } else {
                binding.bottomNavigation.removeBadge(R.id.nextStepsDest)
            }
        }
    }

    private fun ensurePermissionsGranted() {
        lifecycleScope.launchWhenStarted {
            viewModel.ensurePermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionResultListener.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
