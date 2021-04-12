package org.ebolapp.features.background.updates.usecases

import org.ebolapp.features.background.updates.notificator.Notificator
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter

interface SetupNotificationChannelsUseCase {
    operator fun invoke()
}

internal class SetupNotificationChannelsUseCaseImpl(
    private val notificator: Notificator
): SetupNotificationChannelsUseCase, Logging {

    override fun invoke() {
        debug("SetupNotificationChannelsUseCase", Filter.BACKGROUND_JOB_LOG)
        notificator.setupRiskMatchesNotificationChannel()
    }
}