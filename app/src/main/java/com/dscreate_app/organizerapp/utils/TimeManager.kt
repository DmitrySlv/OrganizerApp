package com.dscreate_app.organizerapp.utils

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeManager {

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun getTimeFormat(time: String, sharedPref: SharedPreferences): String {
        val defFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val defDate = defFormatter.parse(time)
        val newFormat = sharedPref.getString("time_format_key", DATE_FORMAT)
        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
        return if (defDate != null) {
            newFormatter.format(defDate)
        } else {
            time
        }
    }

    private const val DATE_FORMAT = "hh:mm:ss - dd/MM/yyyy"
}