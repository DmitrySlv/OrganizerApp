package com.dscreate_app.organizerapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeManager {

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private const val DATE_FORMAT = "hh:mm:ss - dd/MM/yyyy"
}