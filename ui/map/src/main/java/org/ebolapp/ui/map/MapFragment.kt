package org.ebolapp.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.ebolapp.presentation.map.*
import org.ebolapp.presentation.map.MapState.*
import org.ebolapp.ui.common.extensions.*
import org.ebolapp.ui.debug.DebugActivity
import org.ebolapp.ui.webview.WebViewActivity
import org.ebolapp.ui.map.databinding.UiMapFragmentBinding
import org.ebolapp.ui.map.views.showDatePickerDialog
import org.ebolapp.ui.map.views.showErrorDialog
import org.ebolapp.ui.map.wrapper.MapWrapper
import org.ebolapp.ui.map.wrapper.setupGoogleMaps
import org.ebolapp.ui.settings.SettingsActivity
import org.ebolapp.utils.DateUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.material.SliderTouchEvent
import reactivecircus.flowbinding.material.touchEvents
import org.ebolapp.ui.legend.LegendActivity


class MapFragment : Fragment(R.layout.ui_map_fragment) {

    private var binding: UiMapFragmentBinding? = null
    private val viewModel by viewModel<MapViewModel>()

    private lateinit var mapWrapper: MapWrapper
    private var loadingPopupJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UiMapFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogleMaps { googleMaps ->
            mapWrapper = MapWrapper(
                uiScope = lifecycleScope,
                context = requireContext(),
                map = googleMaps,
                onRegionSelected = { viewModel.dispatchAction(Action.SelectRegion(it)) },
                onDoneDrawingMapRegions = { viewModel.dispatchAction(Action.LoadingRegions.Done) },
                onMapMovedByUser = { viewModel.dispatchAction(Action.DeselectRegionAndLocation) },
                onMapMoved = { mapRegionBox ->
                    viewModel.dispatchAction(
                        Action.UpdateMapRegionBox(
                            mapRegionBox
                        )
                    )
                }
            ).also {
                it.moveToDefaultLocation()
                viewModel.dispatchAction(Action.LoadRegions(it.mapRegionBox()))
            }
            binding?.map?.post { mapWrapper.mapPadding = binding?.map?.measuredWidth ?: 0 }
            setupViewsActions()
            setupState()
        }
    }

    private fun setupState() {
        observe(viewModel.state) { states ->
            renderDataIsLoading(states)
            renderMapRegions(states)
            renderNotRiskyUserVisits(states)
            renderRiskyUserVisits(states)
            renderMapRegionInfo(states)
            renderMapButtons(states)
            renderMapDateText(states)
            renderDateSlider(states)
            renderMapCurrentLocation(states)
            renderMapPermissionsForLocation(states)
            renderErrors(states)
            zoomToPositions(states)
        }
    }

    private fun renderMapPermissionsForLocation(states: MapStates) {
        val (old, new) = states
        if (old.hasPermissionsForLocation != new.hasPermissionsForLocation) {
            mapWrapper.setHasLocationPermission(new.hasPermissionsForLocation)
        }
    }

    private fun renderDataIsLoading(states: MapStates) {
        val (old, new) = states
        when {
            !old.isLoading && new.isLoading -> {
                loadingPopupJob?.cancel()
                loadingPopupJob = lifecycleScope.launch {
                    withContext(Dispatchers.Default) { delay(LOADING_POPUP_DELAY) }
                    binding?.mapLoadingIndicator?.show()
                }
            }
            else -> {
                loadingPopupJob?.cancel()
                binding?.mapLoadingIndicator?.hide()
            }
        }
    }

    private fun renderMapRegions(states: MapStates) {
        val (old, new) = states
        if (old.regions != new.regions)
            mapWrapper.addMapRegions(new.regions)
    }

    private fun renderMapRegionInfo(states: MapStates) {
        val (old, new) = states
        with(binding?.mapRegionInfo) infoCard@{
            when {
                (old.regionInfoState.isDisplayed && new.regionInfoState.isDisplayed)
                    && (old.regionInfoState.regionInfoCard != new.regionInfoState.regionInfoCard) ->
                    new.regionInfoState.regionInfoCard?.let {
                        this@infoCard?.renderContent(it, ::onInfoButtonClick, ::onSeverityClick)
                    }
                (!old.regionInfoState.isDisplayed && new.regionInfoState.isDisplayed) -> {
                    new.regionInfoState.regionInfoCard?.let {
                        this@infoCard?.renderContent(it, ::onInfoButtonClick, ::onSeverityClick)
                    }
                    this@infoCard?.applySlideTransitionAnimation(Gravity.BOTTOM)
                    this@infoCard?.showNoAnim()

                }
                (old.regionInfoState.isDisplayed && !new.regionInfoState.isDisplayed) -> {
                    new.regionInfoState.regionInfoCard?.let {
                        this@infoCard?.renderContent(it, ::onInfoButtonClick, ::onSeverityClick)
                    }
                    this@infoCard?.applySlideTransitionAnimation(Gravity.BOTTOM)
                    this@infoCard?.goneNoAnim()
                }
                else -> {
                }
            }
        }
    }

    private fun onInfoButtonClick(informationURL: String) {
        WebViewActivity.start(
            context = requireContext(),
            dependencies = WebViewActivity.Dependencies(
                url = informationURL,
                title = context?.getString(R.string.map_region_info_link_name),
                isStatic = false
            )
        )
    }

    private fun onSeverityClick() {
        startActivity(Intent(requireContext(), LegendActivity::class.java))
    }

    private fun renderNotRiskyUserVisits(states: MapStates) {
        val (old, new) = states
        if (old.userNotRiskyVisitsForDay != new.userNotRiskyVisitsForDay)
            mapWrapper.addUserVisitsForDayMarkers(new.userNotRiskyVisitsForDay)
    }

    private fun renderRiskyUserVisits(states: MapStates) {
        val (old, new) = states
        if (old.userRiskyVisitsForDay != new.userRiskyVisitsForDay)
            mapWrapper.addRiskyVisitsForDayMarkers(new.userRiskyVisitsForDay.map { it.visit })
    }

    private fun renderDateSlider(states: MapStates) {
        val (old, new) = states
        with(binding?.mapDaySlider) dateSliderView@{
            when {
                (!old.sliderState.isDisplayed && new.sliderState.isDisplayed) -> {
                    this?.applySlideTransitionAnimation(Gravity.BOTTOM)
                    this?.showNoAnim()
                }
                (old.sliderState.isDisplayed && !new.sliderState.isDisplayed) -> {
                    this?.applySlideTransitionAnimation(Gravity.BOTTOM)
                    this?.goneNoAnim()
                }
                else -> {
                }
            }
            with(this?.dateSlider) {
                if (old.sliderState != new.sliderState) {
                    this@dateSliderView?.renderSliderState(new.sliderState)
                }
            }
        }
    }

    private fun renderMapButtons(states: MapStates) {
        val (_, new) = states
        with(binding) {
            this?.mapCurrentLocation?.checked(new.isLocationButtonChecked)
            this?.mapChooseDate?.checked(new.isCalendarButtonChecked)
            this?.mapDateText?.isVisible = new.isCalendarButtonChecked
            this?.mapRiskBadge?.isVisible = new.sliderState.exposedDays.count() > 0
            this?.mapRiskBadgeText?.text = new.sliderState.exposedDays.count().toString()
        }
    }

    private fun renderMapDateText(states: MapStates) {
        val (_, new) = states
        with(binding) {
            this?.mapDateText?.text = DateUtils.pointFormattedDate(new.sliderState.timestampSec)
            this?.mapDateText?.isVisible = new.isCalendarButtonChecked
        }
    }

    private fun renderMapCurrentLocation(states: MapStates) {
        val (old, new) = states
        if (old.currentLocation != new.currentLocation)
            new.currentLocation?.let { mapWrapper.moveToCurrentLocation(it) }
    }

    private fun zoomToPositions(states: MapStates) {
        val (old, new) = states
        if (old.zoomPositions != new.zoomPositions)
            mapWrapper.moveToShowAllPositions(new.zoomPositions)
    }

    private fun renderErrors(states: MapStates) {
        val (old, new) = states
        if (old.error != new.error) {
            when (new.error) {
                ErrorState.None -> {
                } // Do nothing
                ErrorState.NoInternetError -> {
                    context?.showErrorDialog(
                        titleResId = R.string.no_internet_error_title,
                        messageResId = R.string.no_internet_error_message,
                        positiveResId = R.string.refresh_button,
                        onPositive = {
                            viewModel.dispatchAction(Action.LoadRegions(mapWrapper.mapRegionBox()))
                        },
                        negativeResId = R.string.ok_button
                    )
                }
                ErrorState.NoLocationPermissionError -> {
                } // Todo: handle error }
                ErrorState.LocationSettingsDisabledError -> {
                } // Todo: handle error }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupViewsActions() {
        binding
            ?.mapCurrentLocation
            ?.clicks()
            ?.sample(CLICK_SAMPLE_MILLIS)
            ?.onEach {
                mapWrapper.deselectRegionInternal()
                viewModel.dispatchAction(Action.ClickLocationButton)
            }
            ?.launchIn(lifecycleScope)
        binding
            ?.mapChooseDate
            ?.clicks()
            ?.sample(CLICK_SAMPLE_MILLIS)
            ?.onEach { viewModel.dispatchAction(Action.ClickCalendarButton) }
            ?.launchIn(lifecycleScope)
        binding
            ?.mapDaySlider
            ?.datePickerButton
            ?.clicks()
            ?.sample(CLICK_SAMPLE_MILLIS)
            ?.withLatestFrom(viewModel.state) { _, states -> states }
            ?.onEach {
                showDatePickerDialog(
                    selectedTimestampSec = it.current.sliderState.timestampSec,
                    onSelection = { timestampMillis ->
                        viewModel.dispatchAction(Action.DatePickerSelection(timestampMillis))
                    }
                )
            }
            ?.launchIn(lifecycleScope)
        binding
            ?.mapDaySlider
            ?.dateSlider
            ?.touchEvents()
            ?.onEach { event ->
                if (event is SliderTouchEvent.StopTracking)
                    viewModel.dispatchAction(Action.SliderValueSelection(event.slider.value.toInt()))
            }
            ?.launchIn(lifecycleScope)

        binding?.toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_debug -> {
                    startActivity(Intent(requireContext(), DebugActivity::class.java))
                    true
                }
                R.id.menu_settings -> {
                    startActivity(Intent(requireContext(), SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        const val CLICK_SAMPLE_MILLIS = 400L
        const val LOADING_POPUP_DELAY = 2000L
    }
}

