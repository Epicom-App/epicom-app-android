package org.ebolapp.logging.strategy

import org.ebolapp.logging.entities.LogEntry

interface LogEntryReader {
    fun read(logLine:String): LogEntry
}