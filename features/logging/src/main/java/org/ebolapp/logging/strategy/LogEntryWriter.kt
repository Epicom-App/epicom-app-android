package org.ebolapp.logging.strategy

import org.ebolapp.logging.entities.LogEntry

interface LogEntryWriter {
    fun write(logEntry: LogEntry)
}