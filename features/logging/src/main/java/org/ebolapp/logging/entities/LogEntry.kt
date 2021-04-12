package org.ebolapp.logging.entities

data class LogEntry(
    val timestamp: Long,
    val priority: Priority,
    val tag: String,
    val message: String
)

