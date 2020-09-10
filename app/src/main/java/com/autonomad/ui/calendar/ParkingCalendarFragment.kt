package com.autonomad.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.fragment_parking_calendar.*
import kotlinx.android.synthetic.main.item_calendar_day_legend.*
import kotlinx.android.synthetic.main.item_calendar_header_parking.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.WeekFields
import java.util.*

class ParkingCalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parking_calendar, container, false)
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val daysOfWeek = daysOfWeekFromLocale()

        legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text =
                    daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale("ru", "KZ")).first()
                        .toString()
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColor(ContextCompat.getColor(context, R.color.grey_7))
            }
        }

        exTwoCalendar.setup(
            YearMonth.now().minusMonths(24),
            YearMonth.now().plusMonths(24),
            daysOfWeek.first()
        )
        exTwoCalendar.scrollToMonth(YearMonth.now())

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay

            val textView = with(view) {
                setOnClickListener {
                    if (selectedDate == day.date) {
                        selectedDate = null
                        exTwoCalendar.notifyDayChanged(day)
                    } else {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        exTwoCalendar.notifyDateChanged(day.date)
                        oldDate?.let { exTwoCalendar.notifyDateChanged(oldDate) }
                    }

                }
                return@with this as TextView
            }

        }
        exTwoCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when {
                        day.date == selectedDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.ic_default_blue_round)
                        }
                        day.date == today -> {
                            textView.setTextColorRes(R.color.Red)
                            textView.background = null
                        }
                        day.date < today -> {
                            textView.setTextColorRes(R.color.grey_5)
                            textView.background = null
                        }
                        else -> {
                            textView.setTextColorRes(R.color.Black)
                            textView.background = null
                        }
                    }
                } else {
                    textView.makeInVisible()
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exTwoHeaderText
        }
        exTwoCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                val monthInt = month.yearMonth.month.value
                container.textView.text =
                    "${Month.of(monthInt).getDisplayName(
                        TextStyle.FULL,
                        Locale("ru", "KZ")
                    ).toLowerCase().capitalize()} ${month.year}"
            }
        }
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        save.setOnClickListener {
            val date = selectedDate
            if (date != null) {
                val text = "Selected: ${DateTimeFormatter.ofPattern("d MMMM yyyy").format(date)}"
                Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
            }
            activity?.onBackPressed()
        }

    }

    fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    fun GradientDrawable.setCornerRadius(
        topLeft: Float = 0F,
        topRight: Float = 0F,
        bottomRight: Float = 0F,
        bottomLeft: Float = 0F
    ) {
        cornerRadii = arrayOf(
            topLeft, topLeft,
            topRight, topRight,
            bottomRight, bottomRight,
            bottomLeft, bottomLeft
        ).toFloatArray()
    }

    fun View.makeVisible() {
        visibility = View.VISIBLE
    }

    fun View.makeInVisible() {
        visibility = View.INVISIBLE
    }

    fun View.makeGone() {
        visibility = View.GONE
    }

    fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
        ContextCompat.getDrawable(this, drawable)

    fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

}