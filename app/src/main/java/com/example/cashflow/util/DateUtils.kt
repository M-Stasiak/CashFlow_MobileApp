package com.example.cashflow.util

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(millis: Long, pattern: String = "dd.MM.yyyy"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(millis))
}