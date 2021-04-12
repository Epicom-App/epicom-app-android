package org.ebolapp.features.background.updates.usecases

import androidx.work.*
import org.ebolapp.features.background.updates.workers.UpdateRiskAreaCasesWorker
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.koin.core.component.KoinApiExtension
import java.time.*
import java.util.concurrent.TimeUnit

@KoinApiExtension
interface StartRegionCasesPeriodicSyncUseCase {
    operator fun invoke()
}

@KoinApiExtension
internal class StartRegionCasesPeriodicSyncUseCaseImpl(
    private val workManager: WorkManager
) : StartRegionCasesPeriodicSyncUseCase, Logging {

    override fun invoke() {
        debug(
            "StartRegionCasesPeriodicUpdatesUseCase every $UPDATE_INTERVAL_HRS hours",
            Filter.BACKGROUND_JOB_LOG
        )

        val constraints = Constraints
            .Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val initialDelaySeconds: Long = calculateInitialDelaySeconds()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<UpdateRiskAreaCasesWorker>(Duration.ofHours(UPDATE_INTERVAL_HRS))
                .setConstraints(constraints)
                .setInitialDelay(initialDelaySeconds, TimeUnit.SECONDS)
                .build()

        debug(
            "StartRegionCasesPeriodicUpdatesUseCase will be run in " +
            "${TimeUnit.SECONDS.toHours(initialDelaySeconds)} hours", Filter.BACKGROUND_JOB_LOG
        )
        // Using REPLACE to reschedule every time the job on app start, as KEEP will be degrading
        // over time the intended exact time run + random offset
        workManager.enqueueUniquePeriodicWork(
            PERIODIC_SYNC_OF_REGION_CASES_JOB_ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    private fun calculateInitialDelaySeconds(): Long {
        val nowTimeUtc: OffsetDateTime = Instant.now().atOffset(ZoneOffset.UTC)
        var nextRunTimeUtc: OffsetDateTime = LocalDate.now().atTime(2,0).atOffset(ZoneOffset.UTC)
        if (Duration.between(nowTimeUtc, nextRunTimeUtc).seconds < 0L) {
            nextRunTimeUtc = LocalDate.now().plusDays(1).atTime(2,0).atZone(ZoneOffset.UTC).toOffsetDateTime()
        }
        val timeTillNextRun = Duration.between(nowTimeUtc, nextRunTimeUtc)
        // fetch starts at 2 am + random (0 to 45 minutes) to make it less stress full on backend side
        val randomSeconds = (0..45).random() * 60
        return timeTillNextRun.seconds + randomSeconds
    }

    companion object {
        private const val PERIODIC_SYNC_OF_REGION_CASES_JOB_ID = "periodicRiskAreaCasesSyncJobId"
        private const val UPDATE_INTERVAL_HRS = 24L
    }
}