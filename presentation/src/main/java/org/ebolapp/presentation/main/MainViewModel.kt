package org.ebolapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.features.permissions.usecases.RequestPermissionUseCase
import org.ebolapp.features.riskmatching.usecase.ObserveNotNotifiedRiskMatchesUseCase
import org.ebolapp.features.settings.OnboardingUseCase
import org.ebolapp.load.regions.preferences.RegionsImportState
import org.ebolapp.load.regions.usecases.ImportStateCheckUseCase

class MainViewModel(
    private val requestPermissionUseCase: RequestPermissionUseCase,
    observeNotNotifiedRiskMatchesUseCase: ObserveNotNotifiedRiskMatchesUseCase,
    importCheckStateUseCase: ImportStateCheckUseCase,
    onboardingUseCase: OnboardingUseCase
) : ViewModel() {

    private val _newRiskMatchesCount = MutableStateFlow(0)
    val newRiskMatchesCount: StateFlow<Int> = _newRiskMatchesCount

    private val _regionsImportState = MutableStateFlow(importCheckStateUseCase.check())
    val regionsImportState: StateFlow<RegionsImportState> = _regionsImportState

    val screenState =
        combine(regionsImportState, onboardingUseCase.check()) { regionsImport, onboardingFinished ->
            when {
                onboardingFinished != OnboardingUseCase.State.FINISHED-> ScreenState.ONBOARDING
                regionsImport !is RegionsImportState.Complete -> ScreenState.LOADING
                else -> ScreenState.NORMAL
            }
        }.distinctUntilChanged()

    init {
        observeNotNotifiedRiskMatchesUseCase()
            .onEach { riskMatches ->
                _newRiskMatchesCount.value = riskMatches.count()
            }.launchIn(viewModelScope)
        importCheckStateUseCase
            .observe()
            .onEach {
                _regionsImportState.value = it
            }.launchIn(viewModelScope)
    }

    suspend fun ensurePermissionsGranted() {
        requestPermissionUseCase.invoke(
            Permission.BACKGROUND_LOCATION,
            Permission.FOREGROUND_LOCATION
        )
    }

    enum class ScreenState {
        ONBOARDING,
        LOADING,
        NORMAL
    }

}
