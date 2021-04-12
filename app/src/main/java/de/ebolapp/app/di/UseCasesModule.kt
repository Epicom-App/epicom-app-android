package de.ebolapp.app.di

import de.ebolapp.app.usecases.RunOnAppStartUseCase
import org.ebolapp.appcenter.AppCenterFactory
import org.ebolapp.features.cases.MapRegionsCasesUseCaseFactory
import org.ebolapp.features.debug.DebugUseCaseFactory
import org.ebolapp.features.regions.MapRegionsUseCaseFactory
import org.ebolapp.features.riskmatching.RiskMatchingUseCaseFactory
import org.ebolapp.features.settings.OnboardingUseCaseFactory
import org.ebolapp.features.staticPages.StaticPagesUseCaseFactory
import org.ebolapp.features.visits.VisitsUseCaseFactory
import org.ebolapp.load.regions.LoadRegionsUseCaseFactory
import org.ebolapp.logging.usecases.LoggingUseCaseFactory
import org.ebolapp.widget.WidgetFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module


@OptIn(KoinApiExtension::class)
val useCasesModule = module {

    // Cases
    single { MapRegionsCasesUseCaseFactory(endpoints = get(), databaseWrapper = get()) }
    factory { (get() as MapRegionsCasesUseCaseFactory).createGetLegendUseCase() }
    factory { (get() as MapRegionsCasesUseCaseFactory).createSaveCases() }

    // Regions Import
    single { LoadRegionsUseCaseFactory(databaseWrapper = get(), context = get())}
    factory { get<LoadRegionsUseCaseFactory>().createImportMapRegionsUseCase() }
    factory { get<LoadRegionsUseCaseFactory>().createImportCheckStateUseCase() }

    // Regions Query
    single { MapRegionsUseCaseFactory(databaseWrapper = get(), endpoints = get()) }
    factory { get<MapRegionsUseCaseFactory>().createGetMapRegionsWithGeometryUseCase() }
    factory { get<MapRegionsUseCaseFactory>().createGetMapRegionByPositionUseCase() }
    factory { get<MapRegionsUseCaseFactory>().createGetMapRegionInfoUseCase() }
    factory { get<MapRegionsUseCaseFactory>().createCreateMapRegionsUseCase(
            statesFileReader = get(StatesJsonQualifier),
            districtFileReader = get(StatesDistrictsJsonQualifier)
        )
    }

    // Visits
    single { VisitsUseCaseFactory(databaseWrapper = get()) }
    factory { (get() as VisitsUseCaseFactory).createSaveUserLocationsUseCase() }
    factory { (get() as VisitsUseCaseFactory).createGetUserLocationsUseCase() }
    factory { (get() as VisitsUseCaseFactory).createGetVisitsForDayUseCase() }

    // Matching
    single { RiskMatchingUseCaseFactory(databaseWrapper = get(), endpoints = get()) }
    factory { (get() as RiskMatchingUseCaseFactory).createObserveRiskDatesOffsetsFromCurrentDateForPeriod() }
    factory { (get() as RiskMatchingUseCaseFactory).createObserveRiskMatchesForTodayUseCase() }
    factory { (get() as RiskMatchingUseCaseFactory).createGetNotRiskyVisitsForDayUseCase() }
    factory { (get() as RiskMatchingUseCaseFactory).createGetRiskMatchesForDayUseCase() }
    factory { (get() as RiskMatchingUseCaseFactory).createGetNotNotifiedRiskMatchesUseCase() }
    factory { (get() as RiskMatchingUseCaseFactory).createObserveNotNotifiedRiskMatchesUseCase() }
    factory { (get() as RiskMatchingUseCaseFactory).createMarkAsNotifiedRiskMatchesUseCase() }

    // Debug
    single { DebugUseCaseFactory(databaseWrapper = get()) }
    factory { (get() as DebugUseCaseFactory).createCasesDebugInfoUseCase() }
    factory { (get() as DebugUseCaseFactory).createCasesLegendDebugInfoUseCase() }
    factory { (get() as DebugUseCaseFactory).createMapRegionsDebugInfoUseCase() }
    factory { (get() as DebugUseCaseFactory).createUserLocationsDebugInfoUseCase() }
    factory { (get() as DebugUseCaseFactory).createVisitsDebugInfoUseCase() }
    factory { (get() as DebugUseCaseFactory).createRiskMatchesDebugInfoUseCase() }
    factory { (get() as DebugUseCaseFactory).createRiskMatchNotificationsDebugInfoUseCase() }

    // Logging
    single { LoggingUseCaseFactory(context = get()) }
    factory { (get() as LoggingUseCaseFactory).createSetupLoggingUseCase() }

    // App Center
    single { AppCenterFactory(context = get(), appCenterSecret = get(AppCenterSecretQualifier)) }
    factory { get<AppCenterFactory>().createSetupAppCenterUseCase() }

    // Initializer
    factory {
        RunOnAppStartUseCase(
            context = get(),
            setupLoggingUseCase = get(),
            setupNotificationChannelsUseCase = get(),
            startRegionCasesPeriodicSyncUseCase = get(),
            startRiskMatchingPeriodicNotifyUseCase = get(),
            locationService = get(),
            initializePermissionHandlingUseCase = get(),
            setupAppCenterUseCase = get(),
            importMapRegionsUseCase = get(),
            startWidgetUpdaterUseCase = get()
        )
    }

    // Connectivity
    single { WidgetFactory(context = get(), get()) }
    factory { get<WidgetFactory>().createStartWidgetUpdaterUseCase() }

    // Settings
    single { OnboardingUseCaseFactory.create(androidContext()) }

    //Static pages
    single { StaticPagesUseCaseFactory(databaseWrapper = get()) }
    factory { (get() as StaticPagesUseCaseFactory).createGetStaticPageUseCase() }

}
