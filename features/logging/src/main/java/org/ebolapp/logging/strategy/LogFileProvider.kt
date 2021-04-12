package org.ebolapp.logging.strategy

import java.io.File

interface LogFileProvider {
    fun provideLogFile(path: String, name: String): File?
}

fun createFileIfNotExists(logFile: File): Boolean {
    return if (logFile.exists()) {
        true
    } else {
        val parentDir = logFile.parentFile
        if (parentDir.exists()) {
            logFile.createNewFile()
        } else {
            val createdParentDirs = parentDir.mkdirs()
            if (createdParentDirs) {
                logFile.createNewFile()
            } else {
                false
            }
        }
    }
}