package com.nate.frequentask

import java.text.SimpleDateFormat

fun Long.displayDate(): String {
    return SimpleDateFormat("dd/MM").format(this)
}