package org.ebolapp.logging.strategy

import java.io.File
import java.io.IOException

class SessionsBasedLogFileProvider : LogFileProvider {

    override fun provideLogFile(path: String, name: String): File? {
        val logFile = File(path, name)
        val createLogFile = try {
            createFileIfNotExists(logFile)
        } catch (ioe: IOException) {
            false
        }
        return if (createLogFile) logFile else null
    }
}

