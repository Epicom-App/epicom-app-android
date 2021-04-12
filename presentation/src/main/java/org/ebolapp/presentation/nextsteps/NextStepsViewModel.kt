package org.ebolapp.presentation.nextsteps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.ebolapp.features.regions.usecases.GetMapRegionInfoUseCase
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.features.riskmatching.usecase.MarkAsNotifiedRiskMatchesUseCase
import org.ebolapp.features.riskmatching.usecase.ObserveRiskMatchForPeriodUseCase
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug

class NextStepsViewModel(
    private val observeRiskMatchingForPeriodUseCase: ObserveRiskMatchForPeriodUseCase,
    private val getMapRegionInfoUseCase: GetMapRegionInfoUseCase,
    private val markAsNotifiedRiskMatchesUseCase: MarkAsNotifiedRiskMatchesUseCase
) : ViewModel(), Logging {

    private val TAG = "NextStepsViewModel"

    private val _state = MutableStateFlow(
        NextStepsStates(
            old = NextStepsState.Loading,
            current = NextStepsState.Loading
        )
    )
    val state: Flow<NextStepsStates> = _state

    private val _effects = MutableSharedFlow<Effect>()
    private val _actions = MutableSharedFlow<Action>()

    init {
        setupActionsHandler()
        setupEffectHandler()
    }

    private fun setupData(action: Action.SetupData) {
        dispatchEffect(Effect.ObserveRiskMatches)
        dispatchEffect(Effect.MarkAsAcknowledged)
    }

    private fun updateRiskMatchesAction(action: Action.UpdateRiskMatches) {
        setState {
            if (action.riskMatches.isEmpty())
                NextStepsState.RiskMatchesEmpty
            else
                NextStepsState.RiskMatches(riskMatches = action.riskMatches)
        }
    }

    private fun observeRiskMatchesEffect() {
        observeRiskMatchingForPeriodUseCase(daysPeriod = 100)
            .onEach { riskMatchesList ->
                debug("observeRiskMatches SUCCESS $riskMatchesList", TAG)
                riskMatchesList
                    .sortedWith(compareByDescending<RiskMatch> { it.visitId }
                        .thenByDescending { it.dayStartTimestampSec }
                    )
                    .map { riskMatch ->
                        riskMatch.toRiskMatchListItem(
                            locationName = getLocationName(riskMatch),
                            contactNumber = getContactNumber(riskMatch)
                        )
                    }
                    .let { riskMatchesListItems ->
                        dispatchAction(Action.UpdateRiskMatches(riskMatchesListItems))
                    }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private suspend fun getLocationName(riskMatch: RiskMatch): String {
        return getMapRegionInfoUseCase(
            riskMatch.regionId,
            riskMatch.dayStartTimestampSec
        )?.name ?: "Unknown"
    }

    private suspend fun getContactNumber(riskMatch: RiskMatch): String {
        // Todo: Probably implement a lookup table for health authority phone number
        return "123-456-789000-22"
    }

    private fun markAsAcknowledgedEffect() {
        debug("markAsAcknowledged START", TAG)
        viewModelScope.launch {
            markAsNotifiedRiskMatchesUseCase(emptyList())
        }
    }

    private fun setupActionsHandler() {
        _actions
            .onEach { action ->
                when (action) {
                    is Action.SetupData -> setupData(action)
                    is Action.UpdateRiskMatches -> updateRiskMatchesAction(action)
                }
            }
            .shareIn(viewModelScope, SharingStarted.Lazily)
            .launchIn(viewModelScope)
    }

    private fun setupEffectHandler() {
        _effects
            .onEach { effect ->
                when (effect) {
                    is Effect.ObserveRiskMatches -> observeRiskMatchesEffect()
                    is Effect.MarkAsAcknowledged -> markAsAcknowledgedEffect()
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun dispatchAction(action: Action) {
        viewModelScope.launch { _actions.emit(action) }
    }

    private fun dispatchEffect(effect: Effect) {
        viewModelScope.launch(Dispatchers.IO) { _effects.emit(effect) }
    }

    private fun setState(newState: (currentState: NextStepsState) -> NextStepsState) {
        with(_state) {
            value = value.copy(old = value.current, current = newState(value.current))
        }
    }

    private fun getState(): NextStepsState {
        return _state.value.current
    }
}

data class NextStepsStates(
    val old: NextStepsState,
    val current: NextStepsState
)

sealed class NextStepsState {
    object Loading : NextStepsState()
    object RiskMatchesEmpty: NextStepsState()
    data class RiskMatches(val riskMatches: List<RiskMatchListItem>): NextStepsState()
}

sealed class Action {
    object SetupData: Action()
    class UpdateRiskMatches(val riskMatches: List<RiskMatchListItem>) : Action()
}

sealed class Effect {
    object ObserveRiskMatches : Effect()
    object MarkAsAcknowledged: Effect()
}