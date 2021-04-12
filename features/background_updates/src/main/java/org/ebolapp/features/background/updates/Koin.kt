package org.ebolapp.features.background.updates

import androidx.work.WorkManager
import org.ebolapp.features.background.updates.notificator.Notificator
import org.ebolapp.features.background.updates.notificator.NotificatorImpl
import org.ebolapp.features.background.updates.usecases.*
import org.ebolapp.features.background.updates.usecases.StartRegionCasesPeriodicSyncUseCaseImpl
import org.ebolapp.features.background.updates.usecases.StartRiskMatchingPeriodicNotifyUseCaseImpl
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val backgroundUpdatesModule = module {

    factory { WorkManager.getInstance(get()) }

    factory<StartRegionCasesPeriodicSyncUseCase> {
        StartRegionCasesPeriodicSyncUseCaseImpl(
            workManager = get()
        )
    }

    factory<StartRiskMatchingPeriodicNotifyUseCase> {
        StartRiskMatchingPeriodicNotifyUseCaseImpl(
            workManager = get()
        )
    }

    factory<Notificator> {
        NotificatorImpl(
            context = get(),
            markAsNotifiedRiskMatchesUseCase = get()
        )
    }

    factory<SetupNotificationChannelsUseCase> {
        SetupNotificationChannelsUseCaseImpl(notificator = get())
    }
}