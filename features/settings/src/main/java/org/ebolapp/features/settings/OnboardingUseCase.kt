package org.ebolapp.features.settings

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface OnboardingUseCase {

    fun check(): Flow<State>
    fun wasStarted()
    fun wasFinished()
    fun restart()

    enum class State {
        FULL_ONBOARDING,
        OPTIONAL_ONBOARDING,
        FINISHED,
    }
}

object OnboardingUseCaseFactory {
    fun create(context: Context): OnboardingUseCase =
        OnboardingStateUseCaseImpl(
            onboardingSettingDataSource = OnboardingSettingDataSource(context)
        )
}

internal class OnboardingStateUseCaseImpl(
    private val onboardingSettingDataSource: OnboardingSettingDataSource
) : OnboardingUseCase {
    private val state = MutableStateFlow(getState())

    override fun check() = state

    override fun wasStarted() {
        onboardingSettingDataSource.setWasFinished(true)
    }

    override fun wasFinished() {
        onboardingSettingDataSource.setWasFinished(true)
        state.value = getState()
    }

    override fun restart() {
        state.value = OnboardingUseCase.State.OPTIONAL_ONBOARDING
    }

    private fun getState() = when (onboardingSettingDataSource.getWasFinished()) {
        true -> OnboardingUseCase.State.FINISHED
        false -> OnboardingUseCase.State.FULL_ONBOARDING
    }

}

