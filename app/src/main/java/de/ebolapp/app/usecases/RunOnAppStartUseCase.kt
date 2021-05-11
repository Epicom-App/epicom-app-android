package de.ebolapp.app.usecases

import android.content.Context
import de.ebolapp.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.ebolapp.appcenter.SetupAppCenterDiagnosticsUseCase
import org.ebolapp.features.background.updates.usecases.SetupNotificationChannelsUseCase
import org.ebolapp.features.background.updates.usecases.StartRegionCasesPeriodicSyncUseCase
import org.ebolapp.features.background.updates.usecases.StartRiskMatchingPeriodicNotifyUseCase
import org.ebolapp.features.geotracking.location.LocationService
import org.ebolapp.features.permissions.usecases.InitializePermissioHandlingUseCase
import org.ebolapp.load.regions.usecases.ImportMapRegionsUseCase
import org.ebolapp.logging.Logging
import org.ebolapp.logging.usecases.SetupLoggingUseCase
import org.koin.core.component.KoinApiExtension
import kotlin.coroutines.CoroutineContext

@KoinApiExtension
class RunOnAppStartUseCase(
    private val setupLoggingUseCase: SetupLoggingUseCase,
    private val setupNotificationChannelsUseCase: SetupNotificationChannelsUseCase,
    private val startRegionCasesPeriodicSyncUseCase: StartRegionCasesPeriodicSyncUseCase,
    private val startRiskMatchingPeriodicNotifyUseCase: StartRiskMatchingPeriodicNotifyUseCase,
    private val locationService: LocationService,
    private val initializePermissionHandlingUseCase: InitializePermissioHandlingUseCase,
    private val setupAppCenterUseCase: SetupAppCenterDiagnosticsUseCase,
    private val importMapRegionsUseCase: ImportMapRegionsUseCase
) : CoroutineScope, Logging {

    override val coroutineContext: CoroutineContext = SupervisorJob()

    operator fun invoke() {
        setupLoggingUseCase(BuildConfig.DEBUG)
        setupAppCenterUseCase(BuildConfig.DEBUG)
        setupNotificationChannelsUseCase()
        startRegionCasesPeriodicSyncUseCase()
        startRiskMatchingPeriodicNotifyUseCase()
        locationService.startBackgroundCollection()
        initializePermissionHandlingUseCase()
        launch { importMapRegionsUseCase() }
    }
}

