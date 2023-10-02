package com.nate.frequentask

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun Long.displayDate(): String {
    val today = SimpleDateFormat("dd/MM/yyyy").format(Date())
    val tomorrow = SimpleDateFormat("dd/MM/yyyy").format(Date().time.plus(TimeUnit.DAYS.toMillis(1)))
    val yesterday = SimpleDateFormat("dd/MM/yyyy").format(Date().time.minus(TimeUnit.DAYS.toMillis(1)))
    return when (SimpleDateFormat("dd/MM/yyyy").format(Date(this))) {
        today -> stringResource(id = R.string.today)
        tomorrow -> stringResource(id = R.string.tomorrow)
        yesterday -> stringResource(id = R.string.yesterday)
        else -> SimpleDateFormat("dd/MM").format(this)
    }
}