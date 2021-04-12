package org.ebolapp.ui.navigation

sealed class Screen {
    object Settings: Screen()
    object Imprint: Screen()
    object Licenses : Screen()
    object DataPrivacy: Screen()
    object About: Screen()
    object Onboarding: Screen()
}