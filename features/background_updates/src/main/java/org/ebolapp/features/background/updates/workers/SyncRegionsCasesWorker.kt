package org.ebolapp.features.background.updates.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ebolapp.features.cases.usecases.SaveMapRegionsCasesByTimestampSecUseCase
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.ebolapp.utils.DateUtils
import org.ebolapp.utils.format
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.atomic.AtomicInteger

private const val MAX_RETRY_COUNT = 3

@KoinApiExtension
internal class UpdateRiskAreaCasesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent, Logging {

    private val saveRiskAreaCasesByTimestampUseCase
            by inject<SaveMapRegionsCasesByTimestampSecUseCase>()

    private var retryCount = AtomicInteger(0)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val timestampStartOfDaySec = DateUtils.dayStartTimestamp(DateUtils.nowTimestampSec())
        debug("UpdateRiskAreaCasesWorker - start - ${timestampStartOfDaySec.format()}", Filter.BACKGROUND_JOB_LOG)
        try {
            saveRiskAreaCasesByTimestampUseCase(timestampStartOfDaySec)
            debug("UpdateRiskAreaCasesWorker - SUCCESS - ${timestampStartOfDaySec.format()}", Filter.BACKGROUND_JOB_LOG)
            onSuccessWorkResult()
        } catch (x: Throwable) {
            debug("UpdateRiskAreaCasesWorker - ERROR - ${timestampStartOfDaySec.format()}", Filter.BACKGROUND_JOB_LOG, x)
            onErrorWorkResult()
        }
    }

    private fun onSuccessWorkResult(): Result = run {
        retryCount.set(0)
        Result.success()
    }

    private fun onErrorWorkResult(): Result = run {
        when (retryCount.incrementAndGet() > MAX_RETRY_COUNT) {
            true -> {
                retryCount.set(0)
                Result.failure()
            }
            false -> Result.retry()
        }
    }
}