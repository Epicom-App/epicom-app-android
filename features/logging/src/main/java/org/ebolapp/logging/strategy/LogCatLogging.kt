package org.ebolapp.logging.strategy

import android.util.Log
import org.ebolapp.logging.entities.LogEntry

internal class LogCatLogging: LogEntryWriter {

    override fun write(logEntry: LogEntry) {
        Log.println(logEntry.priority.level, logEntry.tag, logEntry.message)
    }

}