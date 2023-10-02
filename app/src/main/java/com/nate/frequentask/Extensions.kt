package com.nate.frequentask

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun Long.displayDate(): String {
    return Date(this).displayDate()

}

fun Long.displayVal(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this))
}

@Composable
private fun Date.displayDate(): String {
    val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val tomorrow =
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(Date().time.plus(TimeUnit.DAYS.toMillis(1)))
    val yesterday =
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(Date().time.minus(TimeUnit.DAYS.toMillis(1)))
    return when (SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)) {
        today -> stringResource(id = R.string.today)
        tomorrow -> stringResource(id = R.string.tomorrow)
        yesterday -> stringResource(id = R.string.yesterday)
        else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(this)
    }
}
