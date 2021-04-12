package org.ebolapp.features.background.updates.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ebolapp.features.background.updates.notificator.Notificator
import org.ebolapp.features.riskmatching.usecase.GetNotNotifiedRiskMatchesUseCase
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@KoinApiExtension
internal class NotifyRiskMatchesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent, Logging {

    private val getNotNotifiedRiskMatchesUseCase by inject<GetNotNotifiedRiskMatchesUseCase>()
    private val notificator by inject<Notificator>()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        debug("NotifyRiskMatchesWorker - start", Filter.BACKGROUND_JOB_LOG)
        with(getNotNotifiedRiskMatchesUseCase()) {
            if (this.isNotEmpty()) {
                debug("NotifyRiskMatchesWorker - $this", Filter.BACKGROUND_JOB_LOG)
                notificator.notifyNewRiskMatches(this)
            } else {
                debug("NotifyRiskMatchesWorker - no new risk matches - $this", Filter.BACKGROUND_JOB_LOG)
            }
        }
        Result.success()
    }
}