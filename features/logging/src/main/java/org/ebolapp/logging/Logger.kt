package org.ebolapp.logging

import androidx.annotation.WorkerThread
import org.ebolapp.logging.entities.LogEntry
import org.ebolapp.logging.printer.LogPrinter
import org.ebolapp.logging.printer.PrinterConfig
import org.ebolapp.logging.entities.Priority


object Logger {

    private var printer: LogPrinter = LogPrinter(PrinterConfig.build())

    fun usePrinterConfig(printerConfig: PrinterConfig) {
        printer = LogPrinter(printerConfig)
    }

    fun v(message: String, tag: String? = null, throwable: Throwable? = null) {
        printer.log(Priority.VERBOSE, tag, message, throwable)
    }

    fun d(message: String, tag: String? = null, throwable: Throwable? = null) {
        printer.log(Priority.DEBUG, tag, message, throwable)
    }

    fun i(message: String, tag: String? = null, throwable: Throwable? = null) {
        printer.log(Priority.INFO, tag, message, throwable)
    }

    fun w(message: String, tag: String? = null, throwable: Throwable? = null) {
        printer.log(Priority.WARN, tag, message, throwable)
    }

    fun e(message: String, tag: String? = null, throwable: Throwable? = null) {
        printer.log(Priority.ERROR, tag, message, throwable)
    }

    fun wtf(message: String, tag: String? = null, throwable: Throwable? = null) {
        printer.log(Priority.WTF, tag, message, throwable)
    }

    @WorkerThread
    fun loadCurrentLog(): List<LogEntry> {
        return printer.loadCurrentLog()
    }

    @WorkerThread
    fun deleteCurrentLog() {
        return printer.deleteCurrentLog()
    }

}