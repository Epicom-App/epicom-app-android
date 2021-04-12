package org.ebolapp.ui.settings.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import org.ebolapp.ui.navigation.ScreenNavigator
import org.ebolapp.ui.settings.SettingsFragment

class SettingsFragmentFactory(private val navigator: ScreenNavigator) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            SettingsFragment::class.java.name -> SettingsFragment(navigator)
            else -> super.instantiate(classLoader, className)
        }
    }
}