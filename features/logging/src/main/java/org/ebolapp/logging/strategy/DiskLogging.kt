package org.ebolapp.logging.strategy


import org.ebolapp.logging.entities.*
import java.io.File
import java.util.concurrent.Executors

internal open class DiskLogging(
    fileConfig: LogFileConfig
) : LogEntryWriter, LogEntryReader, Exportable, Deletable {

    private val logFilePath = fileConfig.path
    private val logFileName = fileConfig.name
    private val logFileHandler = fileConfig.logFileProvider
    private val singleQueueExecutor = Executors.newSingleThreadExecutor()

    override fun write(logEntry: LogEntry) {
        val logFile = logFileHandler.provideLogFile(logFilePath, logFileName)
        logFile?.let {
            if (it.exists().and(it.isDirectory.not())) {
                writeToLogFileInBackground(it, logEntryToString(logEntry))
            }
        }
    }

    override fun read(logLine: String): LogEntry {
        return logEntryFromString(logLine)
    }

    private fun logEntryToString(logEntry: LogEntry): String {
        return "tmstmp=${logEntry.timestamp}|priority=${logEntry.priority}|tag=${logEntry.tag}|msg=${logEntry.message}"
    }

    private fun logEntryFromString(logEntry: String): LogEntry {
        val logEntryRegex = "tmstmp=(.*)\\|priority=(.*)\\|tag=(.*)\\|msg=(.*)".toRegex()
        val matchGroups = logEntryRegex.find(logEntry)
        val parsed = matchGroups?.groupValues?.toList()?.subList(1,5)
        return LogEntry(
            timestamp = parsed?.get(0)?.toLong() ?: 0L,
            priority = Priority.valueOf(parsed?.get(1) ?: "DEBUG"),
            tag = parsed?.get(2) ?: "TAG",
            message = parsed?.get(3) ?: "MSG"
        )
    }

    private fun writeToLogFileInBackground(logFile: File, log: String) {
        singleQueueExecutor.execute {
            writeToFile(logFile, log)
        }
    }

    @Synchronized
    private fun writeToFile(logFile: File, log: String) {
        val newLineOrEmpty = if (logFile.length() == 0L) "" else "\n"
        logFile.appendText("$newLineOrEmpty$log")
    }

    data class LogFileConfig(val path: String, val name: String, val logFileProvider: LogFileProvider)

    override fun export(): List<LogEntry> {
        val logFile = logFileHandler.provideLogFile(logFilePath, logFileName)
        return logFile?.readLines()?.map { logEntryFromString(it) } ?: emptyList<LogEntry>()
    }

    override fun delete() {
        val logFile = logFileHandler.provideLogFile(logFilePath, logFileName)?.delete()
        logFileHandler.provideLogFile(logFilePath, logFileName)
    }

}