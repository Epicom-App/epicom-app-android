package org.ebolapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.features.permissions.usecases.HasPermissionUseCase
import org.ebolapp.features.permissions.usecases.RequestPermissionUseCase
import org.ebolapp.features.settings.OnboardingUseCase

class OnboardingViewModel(
    private val onboardingUseCase: OnboardingUseCase,
    private val hasPermissionUseCase: HasPermissionUseCase,
    private val requestPermissionUseCase: RequestPermissionUseCase,
) : ViewModel() {

    private val hasPermission = hasPermissionUseCase(Permission.BACKGROUND_LOCATION)

    val availableScreens = hasPermission.map { hasPermission ->
        listOf(
            ScreenState.Screen1,
            ScreenState.Screen2,
            ScreenState.Screen3,
            ScreenState.Screen4(hasPermission)
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val selectedPos = MutableStateFlow(0)

    val isOptional =
        onboardingUseCase.check().map { it == OnboardingUseCase.State.OPTIONAL_ONBOARDING }

    fun userSawOnboarding() {
        onboardingUseCase.wasStarted()
    }

    fun nextScreen() {
        if (selectedPos.value + 1 >= availableScreens.value.count()) {
            onboardingUseCase.wasFinished()
        } else {
            selectedPos.value++
        }
    }

    fun selectPage(position: Int) {
        selectedPos.value = position
    }

    fun requestLocationPermission() {
        viewModelScope.launch {
            requestPermissionUseCase.invoke(
                permissions = setOf(
                    Permission.BACKGROUND_LOCATION,
                    Permission.FOREGROUND_LOCATION
                ),
                force = true
            )
        }
    }

    fun finish() {
        onboardingUseCase.wasFinished()
    }

    sealed class ScreenState {
        object Screen1 : ScreenState()
        object Screen2 : ScreenState()
        object Screen3 : ScreenState()
        data class Screen4(val hasPermission: Boolean) : ScreenState()
    }
}