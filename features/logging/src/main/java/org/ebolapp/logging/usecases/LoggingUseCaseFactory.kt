package org.ebolapp.logging.usecases

import android.content.Context

class LoggingUseCaseFactory(
    val context: Context
) {
    fun createSetupLoggingUseCase(): SetupLoggingUseCase = SetupLoggingUseCaseImpl(context)
}