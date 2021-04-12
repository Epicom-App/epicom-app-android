@file:SuppressWarnings("MagicNumber")

package org.ebolapp.presentation.common

import java.time.*
import java.time.format.DateTimeFormatter

fun nowToTimestampSec(): Long {
    return LocalDateTime
        .now()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli() / 1000
}

fun nowMinusDaysAgoToTimestampSec(daysAgo: Int): Long {
    return LocalDateTime
        .now()
        .minusDays(daysAgo.toLong())
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli() / 1000
}

fun nowMinusDaysAgoToTimestampMillis(daysAgo: Int): Long {
    return LocalDateTime
        .now()
        .minusDays(daysAgo.toLong())
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
}

fun Long.format(pattern: String = "yy-MM-dd HH:mm:ss"): String {
    return Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().format(
            DateTimeFormatter.ofPattern(pattern)
        )
}

internal fun Long.toLocalDate(): LocalDate {
    return Instant
            .ofEpochSecond(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
}

internal fun Long.toLocalDateTime(): LocalDate {
    return Instant
            .ofEpochSecond(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
}

