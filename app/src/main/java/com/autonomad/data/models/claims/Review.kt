package com.autonomad.data.models.claims

import com.autonomad.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class Review(val id: Int, val text: String, val star: Int, val profile: Profile, val created: String) {
    val starsDrawable: Int
        get() = when (star) {
            5 -> R.drawable.ic_5_stars
            4 -> R.drawable.ic_4_stars
            3 -> R.drawable.ic_3_stars
            2 -> R.drawable.ic_2_stars
            else -> R.drawable.ic_1_star
        }
    val timePassed: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.XXX")
            try {
                val date = dateFormat.parse(created.replaceAfter(".", created.replaceBefore("+", ""))) ?: return ""
                val current = Calendar.getInstance().time
                val dif = current.time - date.time
                val seconds = dif / 1000
                return if (seconds < 60) getPlural(seconds, secondForms) else {
                    val minutes = seconds / 60
                    if (minutes < 60) getPlural(minutes, minuteForms) else {
                        val hours = minutes / 60
                        if (hours < 24) getPlural(hours, hourForms) else {
                            val days = hours / 24
                            if (days < Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH))
                                getPlural(days, dayForms)
                            else calculateMonthDif(date, current)
                        }
                    }
                }
            } catch (e: ParseException) {
                return ""
            }
        }
}

fun getPlural(item: Int, forms: PluralForms, withBack: Boolean = true) = getPlural(item.toLong(), forms, withBack)

fun getPlural(item: Long, forms: PluralForms, withBack: Boolean = true) = "$item " + (if (item % 100 / 10 != 1L)
    when {
        item % 10 == 1L -> forms.single
        item % 10 in 2..4 -> forms.few
        else -> forms.many
    }
else forms.many) + if (withBack) " назад" else ""

private fun calculateMonthDif(startDate: Date, endDate: Date): String {
    val start = Calendar.getInstance().apply { time = startDate }
    val end = Calendar.getInstance().apply { time = endDate }
    val yearDif = end.get(Calendar.YEAR) - start.get(Calendar.YEAR)
    var monthDif = end.get(Calendar.MONTH) - start.get(Calendar.MONTH)
    if (end.get(Calendar.DAY_OF_MONTH) < start.get(Calendar.DAY_OF_MONTH)) monthDif--
    monthDif += yearDif * 12
    return if (monthDif < 12) getPlural(monthDif.toLong(), monthsForms) else getPlural(monthDif / 12L, yearForms)
}

data class PluralForms(val single: String, val few: String, val many: String)

private val secondForms = PluralForms("секунду", "секунды", "секунд")
private val minuteForms = PluralForms("минуту", "минуты", "минут")
private val hourForms = PluralForms("час", "часа", "часов")
private val dayForms = PluralForms("день", "дня", "дней")
private val monthsForms = PluralForms("месяц", "месяца", "месяцев")
private val yearForms = PluralForms("год", "года", "лет")