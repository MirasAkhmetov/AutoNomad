package com.autonomad.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateUtil @Throws(IndexOutOfBoundsException::class, NumberFormatException::class) constructor(
    val source: String,
    val pattern: String = "yyyy-MM-ddTHH:mm"
) {
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var second: Int = 0
    var millis: Long = 0

    private val now = Calendar.getInstance()

    val isThisYear: Boolean
        get() = year == now.get(Calendar.YEAR)

    val isThisMonth: Boolean
        get() = month == (now.get(Calendar.MONTH) + 1)

    val isThisDay: Boolean
        get() = day == now.get(Calendar.DAY_OF_MONTH)

    val isToday: Boolean
        get() = isThisDay && isThisMonth && isThisYear

    val monthString: String
        get() = try {
            months[month - 1]
        } catch (e: IndexOutOfBoundsException) {
            ""
        }

    val monthStringShort: String
        get() = try {
            shortMonths[month - 1]
        } catch (e: IndexOutOfBoundsException) {
            ""
        }

    init {
        try {
            var (y, m, d, h, mm) = List(6) { "" }
            var s = ""
            for ((i, c) in pattern.withIndex()) {
                when (c) {
                    'y' -> y += source[i]
                    'M' -> m += source[i]
                    'd' -> d += source[i]
                    'H' -> h += source[i]
                    'm' -> mm += source[i]
                    's' -> s += source[i]
                }
            }
            year = y.toIntOrNull() ?: 0
            month = m.toIntOrNull() ?: 0
            day = d.toIntOrNull() ?: 0
            hour = h.toIntOrNull() ?: 0
            minute = mm.toIntOrNull() ?: 0
            second = s.toIntOrNull() ?: 0
            millis = SimpleDateFormat(pattern.replace("T", "'T'")).parse(source)?.time ?: -1
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun toString() =
        "DateUtil(year: $year, month: $month, day: $day, hour: $hour, minute: $minute, second: $second)"
}