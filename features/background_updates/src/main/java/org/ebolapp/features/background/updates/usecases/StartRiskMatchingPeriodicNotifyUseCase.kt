package org.ebolapp.features.background.updates.usecases

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.ebolapp.features.background.updates.workers.NotifyRiskMatchesWorker
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import java.time.Duration

interface StartRiskMatchingPeriodicNotifyUseCase {
    operator fun invoke()
}

@KoinApiExtension
internal class StartRiskMatchingPeriodicNotifyUseCaseImpl(
    private val workManager: WorkManager
) : StartRiskMatchingPeriodicNotifyUseCase, Logging, KoinComponent {

    override fun invoke() {

        val constraints = Constraints
            .Builder()
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<NotifyRiskMatchesWorker>(Duration.ofHours(UPDATE_INTERVAL_HRS))
                .setConstraints(constraints)
                .build()

        debug(
            "StartRiskMatchingPeriodicChecksUseCase every $UPDATE_INTERVAL_HRS hours",
            Filter.BACKGROUND_JOB_LOG
        )
        workManager.enqueueUniquePeriodicWork(
            PERIODIC_RISK_MATCHES_NOTIFY_JOB_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    companion object {
        private const val PERIODIC_RISK_MATCHES_NOTIFY_JOB_ID = "periodicRiskMatchesNotifyJobId"
        private const val UPDATE_INTERVAL_HRS = 1L
    }
}