package org.ebolapp.logging.strategy

import org.ebolapp.logging.entities.LogEntry

interface Exportable {
    fun export(): List<LogEntry>
}