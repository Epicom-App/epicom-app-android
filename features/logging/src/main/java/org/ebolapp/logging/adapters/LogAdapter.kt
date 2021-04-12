package org.ebolapp.logging.adapters

import org.ebolapp.logging.entities.LogEntry


interface LogAdapter {

    val isLoggable: Boolean

    fun log(logEntry: LogEntry)

}