package org.ebolapp.logging

interface Logging

fun Logging.verbose(message: String, tag: String? = null, throwable: Throwable? = null) {
    Logger.v(message, tag, throwable)
}

fun Logging.debug(message: String, tag: String? = null, throwable: Throwable? = null) {
    Logger.d(message, tag, throwable)
}

fun Logging.info(message: String, tag: String? = null, throwable: Throwable? = null) {
    Logger.i(message, tag, throwable)
}

fun Logging.warn(message: String, tag: String? = null, throwable: Throwable? = null) {
    Logger.w(message, tag, throwable)
}

fun Logging.error(message: String, tag: String? = null, throwable: Throwable? = null) {
    Logger.e(message, tag, throwable)
}

fun Logging.wtf(message: String, tag: String? = null, throwable: Throwable? = null) {
    Logger.wtf(message, tag, throwable)
}
