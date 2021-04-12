package org.ebolapp.logging.usecases

import android.content.Context
import org.ebolapp.logging.Logger
import org.ebolapp.logging.adapters.AndroidLogAdapter
import org.ebolapp.logging.adapters.PersistingLogAdapter
import org.ebolapp.logging.printer.PrinterConfig


interface SetupLoggingUseCase {
    operator fun invoke(isDebug: Boolean)
}

internal class SetupLoggingUseCaseImpl(
    private val context: Context
) : SetupLoggingUseCase {

    override fun invoke(isDebug: Boolean) {
        Logger.usePrinterConfig(
            PrinterConfig.build(
                arrayOf(
                    AndroidLogAdapter.build(isDebug),
                    PersistingLogAdapter.build(context, isDebug)
                )
            )
        )
    }
}