package com.nate.frequentask

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
fun Long.displayDate(): String {
    return Date(this).displayDate()

}

private fun Long.isInthePast(): Boolean {
    return this < LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).toInstant(ZoneId.systemDefault().rules.getOffset(LocalDateTime.now())).toEpochMilli()
}

fun Long.displayGroupVal(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this)).let {
        if (this.isInthePast()) "past" else it
    }
}

private fun yesterday(): String {
    return SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).format(Date().time.minus(TimeUnit.DAYS.toMillis(1)))
}

private fun tomorrow(): String {
    return SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).format(Date().time.plus(TimeUnit.DAYS.toMillis(1)))
}

private fun today(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
}

@Composable
fun String.displayGroupTitle(): String {
    return when (this) {
        "past" -> stringResource(id = R.string.overdue)
        yesterday() -> stringResource(id = R.string.yesterday)
        today() -> stringResource(id = R.string.today)
        tomorrow() -> stringResource(id = R.string.tomorrow)
        else -> this.dropLastWhile { it != '/' }.dropLast(1)
    }
}

@Composable
private fun Date.displayDate(): String {
    return when (SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)) {
        today() -> stringResource(id = R.string.today)
        tomorrow() -> stringResource(id = R.string.tomorrow)
        yesterday() -> stringResource(id = R.string.yesterday)
        else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(this)
    }
}
