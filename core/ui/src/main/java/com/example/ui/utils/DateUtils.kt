package com.example.ui.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    fun todayStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun endOfDay(dayStart: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = dayStart
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    fun startOfWeek(): Long {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun weekDays(): List<Long> {
        val start = startOfWeek()
        return (0..6).map { start + (it * 24L * 60 * 60 * 1000) }
    }

    fun monthDays(): List<Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)

        val max = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        return (1..max).map {
            cal.set(Calendar.DAY_OF_MONTH, it)
            cal.timeInMillis
        }
    }

    fun formatDayNumber(time: Long): String =
        SimpleDateFormat("d", Locale.getDefault()).format(Date(time))

    fun formatWeekName(time: Long): String =
        SimpleDateFormat("EEE", Locale.getDefault()).format(Date(time))

    fun formatMonthName(time: Long): String =
        SimpleDateFormat("MMMM", Locale.getDefault()).format(Date(time))
}