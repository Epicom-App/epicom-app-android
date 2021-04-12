package org.ebolapp.logging.adapters

import org.ebolapp.logging.entities.LogEntry
import org.ebolapp.logging.strategy.LogCatLogging

class AndroidLogAdapter private constructor(
    loggingEnabled: Boolean = false
) : LogAdapter {

    override val isLoggable: Boolean = loggingEnabled

    private val logCatEntryWriter = LogCatLogging()

    override fun log(logEntry: LogEntry) {
        logCatEntryWriter.write(logEntry)
    }

    companion object {
        fun build(
            isLoggingEnabled: Boolean = false
        ) : AndroidLogAdapter {
            return AndroidLogAdapter(isLoggingEnabled)
        }
    }

}