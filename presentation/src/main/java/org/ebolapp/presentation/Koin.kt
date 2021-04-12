package org.ebolapp.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.ebolapp.presentation.debug.database.*
import org.ebolapp.presentation.main.MainViewModel
import org.ebolapp.presentation.map.MapViewModel
import org.ebolapp.presentation.nextsteps.NextStepsViewModel
import org.ebolapp.presentation.webview.DefaultWebViewModel
import org.ebolapp.presentation.webview.StaticPageWebViewModel
import org.ebolapp.presentation.webview.WebViewViewModel
import org.ebolapp.presentation.widget.CacheWidgetInfoUseCase
import org.ebolapp.presentation.widget.CacheWidgetInfoUseCaseImpl
import org.ebolapp.presentation.onboarding.OnboardingViewModel
import org.ebolapp.presentation.widget.WidgetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
@FlowPreview
val presentationModule = module {
    viewModel {
        MainViewModel(
            requestPermissionUseCase = get(),
            observeNotNotifiedRiskMatchesUseCase = get(),
            importCheckStateUseCase = get(),
            onboardingUseCase = get()
        )
    }
    viewModel {
        MapViewModel(
            getMapRegionsGeometryUseCase = get(),
            getMapRegionInfoUseCase = get(),
            getNotRiskyVisitsForDayUseCase = get(),
            getRiskyMatchesForDayUseCase = get(),
            observeRiskDatesOffsetUseCase = get(),
            getCurrentPositionUseCase = get(),
            hasPermissionsUseCase = get()
        )
    }
    viewModel {
        NextStepsViewModel(
            observeRiskMatchingForPeriodUseCase = get(),
            getMapRegionInfoUseCase = get(),
            markAsNotifiedRiskMatchesUseCase = get()
        )
    }
    viewModel {
        StaticPageWebViewModel(
            getStaticPageUseCase = get()
        )
    }
    viewModel { DefaultWebViewModel() }
    factory<CacheWidgetInfoUseCase> { CacheWidgetInfoUseCaseImpl(context = get()) }
    factory {
        WidgetViewModel(
            getMapRegionByPositionUseCase = get(),
            getMapRegionInfoUseCase = get(),
            getCurrentLocationUseCase = get(),
            hasPermissionsUseCase = get(),
            cacheWidgetInfoUseCase = get()
        )
    }
    // Debug
    viewModel { CasesDbViewModel(getMapRegionCasesDbgInfoUseCase = get()) }
    viewModel { LegendDbViewModel(getMapRegionLegendDbgInfoUseCase = get()) }
    viewModel { LocationsDbViewModel(getUserLocationsDbgInfoUseCase = get()) }
    viewModel { VisitsDbViewModel(getVisitsDbgInfoUseCase = get()) }
    viewModel { RiskMatchDbViewModel(getRiskMatchDbgInfoUseCase = get()) }
    viewModel { RiskMatchNotifyDbViewModel(getRiskMatchNotificationDbgInfoUseCase = get()) }
    viewModel {
        OnboardingViewModel(
            onboardingUseCase = get(),
            hasPermissionUseCase = get(),
            requestPermissionUseCase = get()
        )
    }

}
