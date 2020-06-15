package io.polydev.public_transport.utils

import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    fun getDefaultDate(context: Context, date: Date): String {
        val format = DateFormat.getLongDateFormat(context)
        return format.format(date)
    }
}