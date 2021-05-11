package org.ebolapp.presentation.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.ebolapp.features.geotracking.usecases.GetCurrentPositionUseCase
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.features.permissions.usecases.HasPermissionUseCase
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.entities.MapRegionInfo
import org.ebolapp.features.regions.entities.MapRegionWithGeometry
import org.ebolapp.features.regions.usecases.GetMapRegionInfoUseCase
import org.ebolapp.features.regions.usecases.GetMapRegionsWithGeometryUseCase
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.features.riskmatching.usecase.GetNotRiskyVisitsForDayUseCase
import org.ebolapp.features.riskmatching.usecase.GetRiskMatchesForDayUseCase
import org.ebolapp.features.riskmatching.usecase.ObserveRiskDatesOffsetsFromCurrentDateForPeriod
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.presentation.common.nowMinusDaysAgoToTimestampSec
import org.ebolapp.presentation.map.MapState.*
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.NoSuchElementException
import kotlin.coroutines.cancellation.CancellationException
import org.ebolapp.features.regions.entities.Position

class MapViewModel(
    private val getMapRegionsGeometryUseCase: GetMapRegionsWithGeometryUseCase,
    private val getMapRegionInfoUseCase: GetMapRegionInfoUseCase,
    private val getNotRiskyVisitsForDayUseCase: GetNotRiskyVisitsForDayUseCase,
    private val getRiskyMatchesForDayUseCase: GetRiskMatchesForDayUseCase,
    private val observeRiskDatesOffsetUseCase: ObserveRiskDatesOffsetsFromCurrentDateForPeriod,
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,
    private val hasPermissionsUseCase: HasPermissionUseCase
) : ViewModel(), Logging {

    private val TAG = "MapViewModel"

    private val _state = MutableStateFlow(MapStates(
        old = MapState(),
        current = MapState()
    ))
    val state: Flow<MapStates> = _state

    private val _effects = MutableSharedFlow<Effect>()
    private val _actions = MutableSharedFlow<Action>()
    private var _locationsUpdateHandler: Job? = null

    init {
        setupActionsHandler()
        setupEffectHandler()
        dispatchEffect(Effect.LoadVisits(getState().sliderState.timestampSec))
        dispatchEffect(Effect.LoadRiskMatches(getState().sliderState.timestampSec))
        dispatchEffect(Effect.ObserveRiskMatching)
        dispatchEffect(Effect.ObservePermissionChanges)
    }

    private fun setState(newState: (currentState: MapState) -> MapState) {
        with(_state) {
            value = value.copy(old = value.current, current = newState(value.current))
        }
    }

    private fun getState(): MapState {
        return _state.value.current
    }

    fun dispatchAction(action: Action) {
        viewModelScope.launch { _actions.emit(action) }
    }

    private fun setupActionsHandler() {
        _actions.onEach { action ->
            when (action) {

                is Action.UpdateMapRegionBox -> updateMapRegionBox(action)

                is Action.LoadingRegions -> loading(action)
                is Action.LoadRegions -> loadRegions()
                is Action.LoadRegionsSuccess -> loadRegionsSuccess(action.regions)
                is Action.LoadRegionsError -> handleError(action.error)

                is Action.SelectRegion -> selectRegion(action.region)
                is Action.DeselectRegionAndLocation -> deselectRegionsAndLocation()
                is Action.LoadRegionsInfoSuccess -> loadRegionsInfoSuccess(action.regionInfo)
                is Action.LoadRegionsInfoError -> handleError(action.error)

                is Action.ClickLocationButton -> centerToCurrentLocation()
                is Action.LoadLocationSuccess -> loadCurrentLocationSuccess(action.position)
                is Action.LoadLocationError -> handleError(action.error)

                is Action.ClickCalendarButton -> clickCalendarButton()
                is Action.SliderValueSelection -> sliderValueSelection(action.value)
                is Action.DatePickerSelection -> datePickerValueSelection(action.timestampMillis)

                is Action.LoadVisitsSuccess -> loadVisitsSuccess(action.visits)
                is Action.LoadVisitsError -> handleError(action.error)
                is Action.HasLocationPermission -> handleLocationPermissionChange(action)

                is Action.LoadRiskMatchesForDaySuccess -> loadRiskMatchesForDaySuccess(action)
                is Action.ObserveRiskDatesOffsetForPeriodTic -> loadRiskMatchesForPeriodSuccess(action)
                is Action.LoadRiskMatchesError -> { }
            }
        }
        .shareIn(viewModelScope, SharingStarted.Lazily)
        .launchIn(viewModelScope)
    }

    private fun setupLocationsUpdateHandler() {
        _locationsUpdateHandler?.cancel()
        _locationsUpdateHandler = viewModelScope.launch {
            val loadVisitsFlow = _actions.mapNotNull {
                it as? Action.LoadVisitsSuccess
            }.map { action -> action.visits.map { it.position } }

            val loadRiskMatchesFlow = _actions.mapNotNull {
                it as? Action.LoadRiskMatchesForDaySuccess
            }.map { action -> action.riskMatches.map { it.visit }.map { it.position } }

            loadVisitsFlow
                .combine(loadRiskMatchesFlow) { p1, p2 -> p1 + p2 }
                .take(1)
                .collectLatest {
                     updateZoomPositions(positions = it)
                }
        }
    }

    private fun dispatchEffect(effect: Effect) {
        viewModelScope.launch(Dispatchers.IO) { _effects.emit(effect) }
    }

    private fun setupEffectHandler() {
        _effects
            .onEach { effect ->
                when (effect) {
                    is Effect.LoadRegions -> loadRegionsEffect(effect)
                    is Effect.LoadRegionsInfo -> loadRegionInfoEffect(effect)
                    is Effect.LoadVisits -> loadVisitsEffect(effect)
                    is Effect.LoadRiskMatches -> loadRiskMatchesEffect(effect)
                    is Effect.GetCurrentLocation -> getCurrentLocationEffect()
                    is Effect.ObservePermissionChanges -> checkPermissionForLocation()
                    is Effect.ObserveRiskMatching -> observeRiskMatching()
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun updateMapRegionBox(action: Action.UpdateMapRegionBox) {
        setState { oldState ->
            oldState.copy(
                mapBoundingBoxState = oldState.mapBoundingBoxState.copy(
                    box = action.mapRegionBox
                )
            )
        }
        getState().mapBoundingBoxState.box?.let { mapRegionBox ->
            dispatchEffect(Effect.LoadRegions(
                mapRegionBox = mapRegionBox,
                timestampSec = getState().sliderState.timestampSec
            ))
        }
    }

    private fun handleLocationPermissionChange(action: Action.HasLocationPermission) {
        setState { oldState -> oldState.copy(hasPermissionsForLocation = action.has) }
    }

    private fun loadRegions() {
        getState().mapBoundingBoxState.box?.let { mapRegionBox ->
            dispatchEffect(
                Effect.LoadRegions(
                    mapRegionBox = mapRegionBox,
                    timestampSec = getState().sliderState.timestampSec
                )
            )
        }
    }

    private fun loading(loadingRegions: Action.LoadingRegions) {
        setState { oldState ->
            when (loadingRegions) {
                Action.LoadingRegions.Start -> oldState.copy(isLoading = true)
                Action.LoadingRegions.Done -> oldState.copy(isLoading = false)
            }
        }
    }

    private fun loadRegionsSuccess(regionsWithCases: List<MapRegionWithGeometry>) {
        setState { oldState ->
            oldState.copy(
                regions = regionsWithCases
            )
        }
    }

    private fun loadVisitsSuccess(userVisits: List<Visit>) {
        setState { oldState ->
            oldState.copy(
                userNotRiskyVisitsForDay = userVisits
            )
        }
    }

    private fun loadRiskMatchesForDaySuccess(action: Action.LoadRiskMatchesForDaySuccess) {
        setState { oldState ->
            oldState.copy(
                userRiskyVisitsForDay = action.riskMatches
            )
        }
    }

    private fun loadRiskMatchesForPeriodSuccess(action: Action.ObserveRiskDatesOffsetForPeriodTic) {
        setState { oldState ->
            oldState.copy(
                sliderState = oldState.sliderState.copy(
                    exposedDays = oldState.sliderState.accumulateExposedDay(action.offset)
                )
            )
        }
    }

    private fun selectRegion(region: MapRegionWithGeometry) {
        val calendarButtonCheckStatus = !getState().sliderState.isTodaySelected
        setState { oldState ->
            oldState.copy(
                isCalendarButtonChecked = calendarButtonCheckStatus,
                sliderState = oldState.sliderState.copy(isDisplayed = false)
            )
        }
        dispatchEffect(
            Effect.LoadRegionsInfo(
                region.id,
                getState().sliderState.timestampSec
            )
        )
    }

    private fun updateZoomPositions(positions: List<Position>) {
        setState { oldState ->
            oldState.copy(zoomPositions = positions)
        }
        // Drop positions immediately after changing state
        // to make possible next update with the same values
        setState { oldState ->
            oldState.copy(zoomPositions = emptyList())
        }
    }

    private fun deselectRegionsAndLocation() {
        setState { oldState ->
            oldState.copy(
                isLocationButtonChecked = false,
                currentLocation = null,
                regionInfoState = oldState.regionInfoState.copy(
                    isDisplayed = false,
                    regionInfoCard = null
                )
            )
        }
    }

    private fun loadRegionsInfoSuccess(regionInfoCard: MapRegionInfo) {
        setState { oldState ->
            oldState.copy(
                isLocationButtonChecked = false,
                regionInfoState = oldState.regionInfoState.copy(
                    isDisplayed = true,
                    regionInfoCard = regionInfoCard
                )
            )
        }
    }

    private fun centerToCurrentLocation() {
        dispatchEffect(Effect.GetCurrentLocation)
    }

    private fun loadCurrentLocationSuccess(location: Location) {
        setState { oldState ->
            oldState.copy(
                isLocationButtonChecked = true,
                currentLocation = location,
                regionInfoState = oldState.regionInfoState.copy(
                    isDisplayed = false,
                    regionInfoCard = null
                )
            )
        }
    }

    private fun clickCalendarButton() {
        val calendarCheckedState = if (getState().sliderState.isTodaySelected)
            !getState().isCalendarButtonChecked
        else true
        setState { oldState ->
            oldState.copy(
                isCalendarButtonChecked = calendarCheckedState,
                sliderState = oldState.sliderState.copy(
                    isDisplayed = !oldState.sliderState.isDisplayed
                ),
                regionInfoState = oldState.regionInfoState.copy(
                    isDisplayed = false,
                    regionInfoCard = null
                )
            )
        }
    }

    private fun sliderValueSelection(value: Int) {
        setState { oldState ->
            oldState.copy(sliderState = oldState.sliderState.copy(value = value))
        }
        loadRegions()
        setupLocationsUpdateHandler()
        dispatchEffect(Effect.LoadVisits(getState().sliderState.timestampSec))
        dispatchEffect(Effect.LoadRiskMatches(getState().sliderState.timestampSec))
    }

    private fun datePickerValueSelection(timestampMillis: Long) {
        val pickerDay = Instant.ofEpochMilli(timestampMillis).atOffset(ZoneOffset.UTC).toLocalDate()
        val today = LocalDate.now(ZoneOffset.UTC)
        val duration = Duration.between(pickerDay.atStartOfDay(),today.atStartOfDay())
        setState { oldState ->
            oldState.copy(
                sliderState = oldState.sliderState.copy(
                    value = oldState.sliderState.to - duration.toDays().toInt()
                )
            )
        }
        loadRegions()
        setupLocationsUpdateHandler()
        dispatchEffect(Effect.LoadVisits(getState().sliderState.timestampSec))
        dispatchEffect(Effect.LoadRiskMatches(getState().sliderState.timestampSec))
    }

    private fun handleError(error: ErrorState) {
        setState { oldState -> oldState.copy(error = ErrorState.None)}
        setState { oldState -> oldState.copy(error = error) }
    }

    private var loadRegionsJob: Job? = null
    private suspend fun loadRegionsEffect(effect: Effect.LoadRegions) {
        debug("getMapRegionsWithGeometry START", TAG)
        loadRegionsJob?.cancel()
        dispatchAction(Action.LoadingRegions.Done)
        loadRegionsJob = viewModelScope.launch {
            try {
                dispatchAction(Action.LoadingRegions.Start)
                val regions = getMapRegionsGeometryUseCase(
                    mapRegionBox = effect.mapRegionBox,
                    timestampSec = effect.timestampSec
                )
                debug("getMapRegionsWithGeometry SUCCESS ${regions.map { it.id }}", TAG)
                dispatchAction(Action.LoadRegionsSuccess(regions))
            } catch(cx: CancellationException) {
                debug("getMapRegionsWithGeometry ERROR", TAG, cx)
                dispatchAction(Action.LoadRegionsError(ErrorState.None))
            } catch (x: Throwable) {
                debug("getMapRegionsWithGeometry ERROR", TAG, x)
                dispatchAction(Action.LoadRegionsError(ErrorState.NoInternetError))
            }
        }
    }

    private suspend fun loadRegionInfoEffect(effect: Effect.LoadRegionsInfo) {
        debug("getMapRegionInfo START", TAG)
        try {
            getMapRegionInfoUseCase(
                regionId = effect.regionId,
                timestampSec = effect.timestampSec
            )?.let { mapRegionInfo ->
                debug("getMapRegionInfo SUCCESS - $mapRegionInfo", TAG)
                dispatchAction(Action.LoadRegionsInfoSuccess(mapRegionInfo))
            } ?: dispatchAction(Action.LoadRegionsInfoError(ErrorState.None))
        }
        catch (nsex: NoSuchElementException) {
            debug("getMapRegionInfo ERROR", TAG, nsex)
            dispatchAction(Action.LoadRegionsInfoError(ErrorState.None))
        }
        catch (x: Throwable) {
            debug("getMapRegionInfo ERROR", TAG, x)
            dispatchAction(Action.LoadRegionsInfoError(ErrorState.NoInternetError))
        }
    }

    private suspend fun loadVisitsEffect(effect: Effect.LoadVisits) {
        debug("getNotRiskyVisitsForToday START", TAG)
        try {
            if (hasPermissionsUseCase(Permission.FOREGROUND_LOCATION).first()) {
                val visits = getNotRiskyVisitsForDayUseCase(timestampSec = effect.timestampSec)
                debug("getNotRiskyVisitsForToday SUCCESS - visits ids=${visits.map { it.id }}", TAG)
                dispatchAction(Action.LoadVisitsSuccess(visits))
            } else {
                debug("getNotRiskyVisitsForToday ERROR - no location permissions", TAG)
                dispatchAction(Action.LoadVisitsError(ErrorState.NoLocationPermissionError))
            }
        } catch (x: Throwable) {
            debug("getNotRiskyVisitsForToday ERROR", TAG, x)
            dispatchAction(Action.LoadVisitsError(ErrorState.NoLocationPermissionError))
        }
    }

    private suspend fun loadRiskMatchesEffect(effect: Effect.LoadRiskMatches) {
        debug("loadRiskMatchesForToday START", TAG)
        try {
            val riskMatches = getRiskyMatchesForDayUseCase(effect.timestampSec)
            debug("loadRiskMatchesForToday SUCCESS count=${riskMatches.count()}", TAG)
            dispatchAction(Action.LoadRiskMatchesForDaySuccess(riskMatches))
        } catch (t: Throwable) {
            debug("loadRiskMatchesForToday ERROR", TAG, t)
            dispatchAction(Action.LoadRiskMatchesError())
        }
    }

    private fun observeRiskMatching() {
        debug("observeRiskMatchingForPeriod START", TAG)
        val pastDaysToObserve = getState().sliderState.to
        viewModelScope.launch {
            observeRiskDatesOffsetUseCase(daysPeriod = pastDaysToObserve)
                .collectLatest {
                    dispatchAction(Action.ObserveRiskDatesOffsetForPeriodTic(it))
                }
        }
    }

    private suspend fun getCurrentLocationEffect() {
        debug("getCurrentLocation START", TAG)
        try {
            getCurrentPositionUseCase()?.let { currentLocation ->
                debug("getCurrentLocation SUCCESS", TAG)
                dispatchAction(Action.LoadLocationSuccess(currentLocation))
            } ?: dispatchAction(Action.LoadLocationError(ErrorState.NoLocationPermissionError))
        } catch (x: Throwable) {
            debug("getCurrentLocation ERROR", TAG, x)
            dispatchAction(Action.LoadLocationError(ErrorState.NoLocationPermissionError))
        }
    }

    private fun checkPermissionForLocation() {
        debug("checkPermissionForLocation START", TAG)
        viewModelScope.launch {
            hasPermissionsUseCase(
                Permission.FOREGROUND_LOCATION
            ).collectLatest { hasPermission ->
                debug("checkPermissionForLocation hasPermission=$hasPermission", TAG)
                dispatchAction(Action.HasLocationPermission(hasPermission))
            }
        }
    }

}

sealed class Effect {
    class LoadRegions(val mapRegionBox: MapRegionBox, val timestampSec: Long) : Effect()
    class LoadRegionsInfo(val regionId: String, val timestampSec: Long) : Effect()
    class LoadVisits(val timestampSec: Long) : Effect()
    class LoadRiskMatches(val timestampSec: Long): Effect()
    object ObserveRiskMatching : Effect()
    object GetCurrentLocation : Effect()
    object ObservePermissionChanges: Effect()
}

sealed class Action {


    sealed class LoadingRegions : Action() {
        object Start : LoadingRegions()
        object Done : LoadingRegions()
    }

    class UpdateMapRegionBox(val mapRegionBox: MapRegionBox) : Action()

    class LoadRegions(val mapRegionBox: MapRegionBox) : Action()
    class LoadRegionsSuccess(val regions: List<MapRegionWithGeometry>) : Action()

    class LoadRegionsError(val error: ErrorState) : Action()

    class LoadVisitsSuccess(val visits: List<Visit>) : Action()
    class LoadVisitsError(val error: ErrorState) : Action()

    class LoadRegionsInfoSuccess(val regionInfo: MapRegionInfo) : Action()
    class LoadRegionsInfoError(val error: MapState.ErrorState) : Action()

    class LoadLocationSuccess(val position: Location) : Action()
    class LoadLocationError(val error: ErrorState) : Action()

    class LoadRiskMatchesForDaySuccess(val riskMatches: List<RiskMatch>) : Action()
    class ObserveRiskDatesOffsetForPeriodTic(val offset: Int): Action()
    class LoadRiskMatchesError : Action()

    object ClickLocationButton : Action()
    class HasLocationPermission(val has: Boolean) : Action()

    class SelectRegion(val region: MapRegionWithGeometry) : Action()
    object DeselectRegionAndLocation : Action()

    object ClickCalendarButton : Action()
    class DatePickerSelection(val timestampMillis: Long) : Action()
    class SliderValueSelection(val value: Int) : Action()
}

data class MapStates(
    val old: MapState,
    val current: MapState
)

data class MapState(
    val isLoading: Boolean = false,
    val regions: List<MapRegionWithGeometry> = emptyList(),
    val regionInfoState: RegionInfoState = RegionInfoState(),
    val mapBoundingBoxState: MapBoundingBoxState = MapBoundingBoxState(),
    val userNotRiskyVisitsForDay: List<Visit> = emptyList(),
    val userRiskyVisitsForDay: List<RiskMatch> = emptyList(),
    val userRiskyDays: Map<Long,List<RiskMatch>> = emptyMap(),
    val isLocationButtonChecked: Boolean = false,
    val currentLocation: Location? = null,
    val zoomPositions: List<Position> = emptyList(),
    val hasPermissionsForLocation: Boolean = false,
    val isCalendarButtonChecked: Boolean = false,
    val sliderState: SliderState = SliderState(),
    val error: ErrorState = ErrorState.None
) {

    data class MapBoundingBoxState(
        val box: MapRegionBox? = null
    )

    data class RegionInfoState(
        val isDisplayed: Boolean = false,
        val regionInfoCard: MapRegionInfo? = null
    )

    data class SliderState(
        val isDisplayed: Boolean = false,
        val value: Int = 14,
        val from: Int = 0,
        val to: Int = 14,
        val exposedDays: List<Int> = listOf()
    ) {
        val timestampSec = nowMinusDaysAgoToTimestampSec(to - value)
        val isTodaySelected = (to == value)
    }

    sealed class ErrorState {
        object None : ErrorState()
        object NoInternetError : ErrorState()
        object NoLocationPermissionError : ErrorState()
        object LocationSettingsDisabledError : ErrorState()
    }
}






