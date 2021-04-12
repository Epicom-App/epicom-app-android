package org.ebolapp.logging.adapters

import android.content.Context
import org.ebolapp.logging.entities.LogEntry
import org.ebolapp.logging.strategy.Exportable
import org.ebolapp.logging.strategy.LogEntryWriter
import org.ebolapp.logging.strategy.*
import org.ebolapp.logging.strategy.DiskLogging
import java.io.File

class PersistingLogAdapter private constructor(
    private val context: Context,
    private val isLoggingEnabled: Boolean = false
): LogAdapter, Exportable, Deletable {

    private val DEFAULT_FILE_PATH = File(context.filesDir, "/logs/").absolutePath
    private val DEFAULT_FILE_NAME = "disk.log"

    private val diskLogger = DiskLogging(
        DiskLogging.LogFileConfig(
            DEFAULT_FILE_PATH,
            DEFAULT_FILE_NAME,
            SessionsBasedLogFileProvider()
        )
    )

    private val diskLogEntryWriter: LogEntryWriter = diskLogger

    override val isLoggable: Boolean = isLoggingEnabled

    override fun log(logEntry: LogEntry) {
        diskLogEntryWriter.write(logEntry)
    }

    override fun export(): List<LogEntry> {
        return diskLogger.export()
    }

    override fun delete() {
        diskLogger.delete()
    }

    companion object {
        fun build(
            context: Context,
            isLoggingEnabled: Boolean = false
        ) : PersistingLogAdapter {
            return PersistingLogAdapter(
                context = context,
                isLoggingEnabled = isLoggingEnabled
            )
        }
    }
}