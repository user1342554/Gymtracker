package com.example.gymtracker.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    private val shortDisplayFormat = SimpleDateFormat("MMM dd", Locale.US)
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.US)

    fun todayString(): String = dateFormat.format(Date())

    fun formatDate(dateStr: String): String {
        return try {
            val date = dateFormat.parse(dateStr)
            date?.let { displayFormat.format(it) } ?: dateStr
        } catch (e: Exception) {
            dateStr
        }
    }

    fun formatDateShort(dateStr: String): String {
        return try {
            val date = dateFormat.parse(dateStr)
            date?.let { shortDisplayFormat.format(it) } ?: dateStr
        } catch (e: Exception) {
            dateStr
        }
    }

    fun formatTime(millis: Long): String = timeFormat.format(Date(millis))

    fun formatDuration(startMillis: Long, endMillis: Long): String {
        val diffMs = endMillis - startMillis
        val minutes = (diffMs / 1000 / 60).toInt()
        val hours = minutes / 60
        val mins = minutes % 60
        return if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
    }

    fun daysAgo(days: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        return dateFormat.format(cal.time)
    }

    fun getCurrentDayOfWeek(): Int {
        val cal = Calendar.getInstance()
        // Convert Calendar's Sunday=1..Saturday=7 to ISO Monday=1..Sunday=7
        val day = cal.get(Calendar.DAY_OF_WEEK)
        return if (day == Calendar.SUNDAY) 7 else day - 1
    }

    fun parseDate(dateStr: String): Date? {
        return try {
            dateFormat.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }

    fun dateToString(date: Date): String = dateFormat.format(date)
}
