package com.autonomad.ui.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.fragment_expenses_calendar.*
import kotlinx.android.synthetic.main.item_calendar_day_expenses.view.*
import kotlinx.android.synthetic.main.item_calendar_day_legend.*
import kotlinx.android.synthetic.main.item_calendar_header_expenses.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.WeekFields
import java.util.*

class ExpensesCalendarFragment : Fragment() {

    private var startDate: LocalDate? = null

    private var endDate: LocalDate? = null
    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE'\n'd MMM")


    private lateinit var today: LocalDate

    private val startBackground: GradientDrawable by lazy {
        requireContext().getDrawable(R.drawable.background_blue_calendar_start) as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        requireContext().getDrawable(R.drawable.background_blue_calendar_end) as GradientDrawable
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        today = LocalDate.now()
        return inflater.inflate(R.layout.fragment_expenses_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        clear.setOnClickListener {
            startDate = null
            endDate = null
            exFourCalendar.notifyCalendarChanged()
            bindSummaryViews()
        }
        // We set the radius of the continuous selection background drawable dynamically
        // since the view size is `match parent` hence we cannot determine the appropriate
        // radius value which would equal half of the view's size beforehand.
        exFourCalendar.post {
            val radius = ((exFourCalendar.width / 7) / 2).toFloat()
            startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
            endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
        }

        // Set the First day of week depending on Locale
        val daysOfWeek = daysOfWeekFromLocale()
        legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale("ru", "KZ"))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColor(ContextCompat.getColor(context, R.color.grey_7))
            }
        }

        val currentMonth = YearMonth.now()
        exFourCalendar.setup(
            currentMonth.minusMonths(24),
            currentMonth.plusMonths(24),
            daysOfWeek.first()
        )
        exFourCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.exFourDayText
            val roundBgView = view.exFourRoundBgView

            init {
                view.setOnClickListener {
                    val date = day.date
                    if (startDate != null) {
                        if (date < startDate || endDate != null) {
                            startDate = date
                            endDate = null
                        } else if (date != startDate) {
                            endDate = date
                        }
                    } else {
                        startDate = date
                    }
                    exFourCalendar.notifyCalendarChanged()
                    bindSummaryViews()
                }
            }
        }
        exFourCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val roundBgView = container.roundBgView

                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.day.toString()
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                    when {
                        startDate == day.date && endDate == null -> {
                            textView.setTextColorRes(R.color.white)
                            roundBgView.makeVisible()
                            roundBgView.setBackgroundResource(R.drawable.ic_default_blue_round)
                        }
                        day.date == startDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.background = startBackground
                        }
                        startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.color.PrimaryBlue)
                        }
                        day.date == endDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.background = endBackground
                        }
                        day.date == today -> {
                            textView.setTextColorRes(R.color.Black)
                            roundBgView.makeVisible()
                            roundBgView.setBackgroundResource(R.drawable.background_round_pin_code)
                        }
                        day.date < today -> {
                            textView.setTextColorRes(R.color.grey_5)
                        }
                        else -> textView.setTextColorRes(R.color.Black)
                    }

                } else {

                    // This part is to make the coloured selection background continuous
                    // on the blank in and out dates across various months and also on dates(months)
                    // between the start and end dates if the selection spans across multiple months.

                    val startDate = startDate
                    val endDate = endDate
                    if (startDate != null && endDate != null) {
                        // Mimic selection of inDates that are less than the startDate.
                        // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                        // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                        if ((day.owner == DayOwner.PREVIOUS_MONTH
                                    && startDate.monthValue == day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue) ||
                            // Mimic selection of outDates that are greater than the endDate.
                            // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                            // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                            (day.owner == DayOwner.NEXT_MONTH
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue == day.date.monthValue) ||

                            // Mimic selection of in and out dates of intermediate
                            // months if the selection spans across multiple months.
                            (startDate < day.date && endDate > day.date
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue)
                        ) {
                            textView.setBackgroundResource(R.color.PrimaryBlue)
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
        }
        exFourCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthInt = month.yearMonth.month.value
                val monthTitle =
                    "${Month.of(monthInt).getDisplayName(
                        TextStyle.FULL,
                        Locale("ru", "KZ")
                    ).toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }

        exFourSaveButton.setOnClickListener click@{
            val startDate = startDate
            val endDate = endDate
            if (startDate != null && endDate != null) {
                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                val text = "Selected: ${formatter.format(startDate)} - ${formatter.format(endDate)}"
                Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(
                    requireView(),
                    "No selection. Searching all Airbnb listings.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
            activity?.onBackPressed()
        }

        bindSummaryViews()
    }

    private fun bindSummaryViews() {
        if (startDate != null) {
            exFourStartDateText.text = headerDateFormatter.format(startDate)
            exFourStartDateText.setTextColorRes(R.color.grey_4)
        } else {
            exFourStartDateText.text = "Начало"
            exFourStartDateText.setTextColorRes(R.color.grey_4)
        }
        if (endDate != null) {
            exFourEndDateText.text = headerDateFormatter.format(endDate)
            exFourEndDateText.setTextColorRes(R.color.grey_4)
        } else {
            exFourEndDateText.text = "Конец"
            exFourEndDateText.setTextColorRes(R.color.grey_4)
        }

        // Enable save button if a range is selected or no date is selected at all, Airbnb style.
        exFourSaveButton.isEnabled = endDate != null || (startDate == null && endDate == null)
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

    override fun onStart() {
        super.onStart()
        val closeIndicator = requireContext().getDrawableCompat(R.drawable.ic_default_back)?.apply {
            setColorFilter(
                requireContext().getColorCompat(R.color.grey_4),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(closeIndicator)
        requireActivity().window.apply {
            // Update statusbar color to match toolbar color.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor = requireContext().getColorCompat(R.color.white)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                statusBarColor = Color.GRAY
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.apply {
            // Reset statusbar color.
            statusBarColor = requireContext().getColorCompat(R.color.Black)
            decorView.systemUiVisibility = 0
        }
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
