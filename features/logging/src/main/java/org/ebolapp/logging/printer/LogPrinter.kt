package org.ebolapp.logging.printer


import org.ebolapp.logging.entities.LogEntry
import org.ebolapp.logging.strategy.Exportable
import org.ebolapp.logging.entities.Priority
import org.ebolapp.logging.strategy.Deletable


class LogPrinter internal constructor(printerConfig: PrinterConfig) {

    private val adapters = printerConfig.adapters

    internal fun log(
        priority: Priority,
        tag: String?,
        message: String,
        throwable: Throwable?
    ) {
        val logTimestamp = System.currentTimeMillis()
        adapters
            .filter { it.isLoggable }
            .forEach { logAdapter ->
                logAdapter.log(
                    LogEntry(
                        timestamp = logTimestamp,
                        priority = priority,
                        tag = tag ?: "NoTag",
                        message = printMessage(message,throwable)
                    )
                )
            }
    }

    private fun printMessage(
        message: String,
        throwable: Throwable?
    ): String {
        val error = throwable?.localizedMessage?.let { " exception: $it" } ?: ""
        return message + error
    }

    internal fun loadCurrentLog(): List<LogEntry> {
        val exportableLogAdapter = adapters.firstOrNull { it is Exportable }
        return exportableLogAdapter?.let {
            (it as Exportable).export()
        } ?: emptyList()
    }

    internal fun deleteCurrentLog() {
        val exportableLogAdapter = adapters.firstOrNull { it is Exportable }
        exportableLogAdapter?.let { (it as Deletable).delete() }
    }

}