package org.ebolapp.ui.settings.utils

import org.ebolapp.features.settings.OnboardingUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.ebolapp.features.staticPages.network.StaticPages
import org.ebolapp.ui.navigation.Screen
import org.ebolapp.ui.navigation.ScreenNavigator
import org.ebolapp.ui.settings.R
import org.ebolapp.ui.settings.SettingsActivity
import org.ebolapp.ui.webview.WebViewActivity

class SettingsScreenNavigator(
    private val settingsActivity: SettingsActivity,
    private val onboardingUseCase: OnboardingUseCase,
    private val staticPages: StaticPages
) : ScreenNavigator {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun open(screen: Screen) {
        when (screen) {

            Screen.Imprint ->
                WebViewActivity.start(
                    context = settingsActivity,
                    dependencies = WebViewActivity.Dependencies(
                        url = staticPages.imprintURL,
                        title = settingsActivity.resources.getString(R.string.settings_imprint),
                        isStatic = true
                    )
                )

            Screen.DataPrivacy ->
                WebViewActivity.start(
                    context = settingsActivity,
                    dependencies = WebViewActivity.Dependencies(
                        url = staticPages.dataPrivacyURL,
                        title = settingsActivity.resources.getString(R.string.settings_data_privacy),
                        isStatic = true
                    )
                )

            Screen.Licenses ->
                println("Not implemented")

            Screen.About ->
                WebViewActivity.start(
                    context = settingsActivity,
                    dependencies = WebViewActivity.Dependencies(
                        url = staticPages.aboutURL,
                        title = settingsActivity.resources.getString(R.string.settings_about),
                        isStatic = true
                    )
                )

            Screen.Onboarding -> {
                settingsActivity.finish()
                onboardingUseCase.restart()
            }

            else -> {
                // not needed
            }
        }
    }
}
