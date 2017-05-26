package com.academiaexpresssystem.Utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getDateStringRepresentationWithoutTime(date: Date?): String {
        if (date == null) return ""
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }

    fun getTimeStringRepresentation(date: Date?): String {
        if (date == null) return ""
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }

    fun getDateFullString(date: Date?): String {
        if (date == null) return ""

        val format = SimpleDateFormat("d MMM HH:mm", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }

    fun getDateStringRepresentation(date: Date): String {
        val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }

    fun getCalendarFromString(str: String): Calendar? {
        try {
            val calendar = Calendar.getInstance()
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(
                    str.split("\\.".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()[0])
            calendar.time = date

            return calendar
        } catch (e: Exception) {
            return null
        }
    }

    fun isToday(firstDate: Calendar, secondDate: Calendar): Boolean {
        return firstDate.get(Calendar.YEAR) == secondDate.get(Calendar.YEAR) &&
                firstDate.get(Calendar.DAY_OF_YEAR) == secondDate.get(Calendar.DAY_OF_YEAR)
    }
}
