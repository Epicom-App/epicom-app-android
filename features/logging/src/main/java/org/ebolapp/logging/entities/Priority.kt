package org.ebolapp.logging.entities

enum class Priority(val level: Int) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    WTF(7)
}