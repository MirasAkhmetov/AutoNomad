package com.autonomad.ui.claims.user_claims.claims.claimthree

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.claim_user.ServiceAdreska
import com.autonomad.data.models.claim_user.ServiceCreate
import com.autonomad.data.models.claims.ServiceModel
import com.autonomad.ui.bottom_sheet.profile.AddressesBottomDialog
import com.autonomad.utils.BaseFragment
import com.google.gson.Gson
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.fragment_create_threeclaim.*
import kotlinx.android.synthetic.main.fragment_create_threeclaim.btn_next_claim
import kotlinx.android.synthetic.main.fragment_create_threeclaim.exTwoAppBarLayout
import kotlinx.android.synthetic.main.fragment_create_threeclaim.exTwoCalendar
import kotlinx.android.synthetic.main.fragment_create_threeclaim.ic_back
import kotlinx.android.synthetic.main.item_calendar_day_legend.*
import kotlinx.android.synthetic.main.item_calendar_day_parkingtwo.view.*
import kotlinx.android.synthetic.main.item_calendar_header_parkingtwo.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateClaim3 : BaseFragment() {
    private var serviceCreateModel: ServiceCreate? = null
    private var claimServiceModel: ServiceModel? = null
    private var isEdit: Boolean = false
    private var serviceAddress: ServiceAdreska? = null
    private val today = LocalDate.now()

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    var intdate: Int = 0
    var isFirstTime: Boolean = true

    private val startBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.background_black_calendar_start) as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.background_black_calendar_end) as GradientDrawable
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_threeclaim, container, false)
        serviceCreateModel =
            Gson().fromJson(arguments?.getString("serviceCreateModel"), ServiceCreate::class.java)
        claimServiceModel =
            Gson().fromJson(arguments?.getString("claimServiceModel"), ServiceModel::class.java)
        return view;
    }

    override fun initialise() {
        claimServiceModel?.let { setEditInitialViews(it) }
    }

    override fun setObservers() {
    }

    override fun setAdapter() {
        val colors = arrayOf("Начать работу", "Завершить работу", "Период работы")
        val adapter = ArrayAdapter(
            context as Context,
            android.R.layout.simple_spinner_item,
            colors
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter;

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        endDate = null
                        intdate = 0
                        btn_next_claim.isEnabled = true
                        lin2.visibility = View.GONE
                        exTwoAppBarLayout.visibility = View.GONE
                        exTwoCalendar.visibility = View.GONE
                        tvSelectedPeriod.visibility = View.GONE
                        lin1.visibility = View.VISIBLE
                    }
                    1 -> {
                        startDate = null
                        intdate = 1
                        btn_next_claim.isEnabled = true
                        lin1.visibility = View.GONE
                        exTwoAppBarLayout.visibility = View.GONE
                        exTwoCalendar.visibility = View.GONE
                        tvSelectedPeriod.visibility = View.GONE
                        lin2.visibility = View.VISIBLE
                    }
                    else -> {
                        startDate = null
                        endDate = null
                        intdate = 2
                        lin1.visibility = View.GONE
                        lin2.visibility = View.GONE
                        exTwoAppBarLayout.visibility = View.VISIBLE
                        exTwoCalendar.visibility = View.VISIBLE
                        tvSelectedPeriod.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val types = ArrayList<String>()
        types.add("Сегодня")
        types.add("Завтра")
        types.add("Выбрать дату")

    }

    override fun setOnClickListener() {
        text_address.setOnClickListener {
            AddressesBottomDialog(R.id.action_claimthree_to_address) { id, description, longitude, latitude ->
                serviceAddress = ServiceAdreska(description, longitude, latitude)
                text_address.text = description
            }.show(childFragmentManager, "AdressList")
        }

        txtch1.setOnClickListener {
            makeRate()
            startDate = LocalDate.now()
            txtch1.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_3_12dp_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
        txtch2.setOnClickListener {
            makeRate()
            startDate = LocalDate.now().plusDays(1)
            txtch2.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_3_12dp_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }

        }
        txtch3.setOnClickListener {
            makeRate()
            txtch3.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_3_12dp_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            txtch6.text = "Выбрать дату"
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            //textView.text = sdf.format(cal.time)

            val dpd = DatePickerDialog(
                context as Context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    txtch3.text = "$dayOfMonth/$monthOfYear/$year"
                    val calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);

                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    startDate = LocalDate.parse(format.format(calendar.time))
                },
                year,
                month,
                day
            )

            dpd.show()

        }

        txtch4.setOnClickListener {
            makeRate()
            endDate = LocalDate.now()
            txtch4.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_3_12dp_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
        txtch5.setOnClickListener {
            makeRate()
            endDate = LocalDate.now().plusDays(1)
            txtch5.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_3_12dp_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
        txtch6.setOnClickListener {
            makeRate()
            txtch6.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_3_12dp_rectangle)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }

            txtch3.text = "Выбрать дату"
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                context as Context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    txtch6.text = "$dayOfMonth/$monthOfYear/$year"
                    val calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);

                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    endDate = LocalDate.parse(format.format(calendar.time))
                },
                year,
                month,
                day
            )

            dpd.show()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (claimServiceModel != null) isEdit = true
        else {
            startDate = today
            if (isFirstTime && !isEdit) {
                startDate = today
                txtch1.apply {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.background_grey_3_12dp_rectangle
                        )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                isFirstTime = false
            }
        }

        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        exTwoCalendar.post {
            val radius = ((exTwoCalendar.width / 7) / 2).toFloat()
            startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
            endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
        }
        val daysOfWeek = daysOfWeekFromLocale()
        legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale("Ru", "Kz"))
                    .toString() // Locale.ENGLISH
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColorRes(R.color.example_4_grey)
            }
        }
        val currentMonth = YearMonth.now()
        exTwoCalendar.setup(currentMonth, currentMonth.plusMonths(6), daysOfWeek.first())
        exTwoCalendar.scrollToMonth(currentMonth)


        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(
                            today
                        ))
                    ) {
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
                        this@CreateClaim3.exTwoCalendar.notifyCalendarChanged()
                        btn_next_claim.isEnabled =
                            endDate != null || (startDate == null && endDate == null)
                    }
                    setStartEndDateText()
                }
            }
        }

        exTwoCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.view.exTwoDayText
                val roundBgView = container.view.exFourRoundBgView
                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.day.toString()

                    if (day.date.isBefore(today)) {
                        textView.setTextColorRes(R.color.example_4_grey_past)
                    } else {
                        when {
                            startDate == day.date && endDate == null -> {
                                textView.setTextColorRes(R.color.white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.background_round_blacknomer)
                            }
                            day.date == startDate -> {
                                textView.setTextColorRes(R.color.white)
                                //textView.background = startBackground
                                textView.setBackgroundResource(R.drawable.background_black_calendar_start)
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColorRes(R.color.white)
                                textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middleblack)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(R.color.white)
                                textView.setBackgroundResource(R.drawable.background_black_calendar_end)
                            }
                            day.date == today -> {
                                textView.setTextColorRes(R.color.example_4_grey)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.example_4_today_bg)
                            }
                            else -> textView.setTextColorRes(R.color.example_4_grey)
                        }
                    }
                } else {
                    val startDate = startDate
                    val endDate = endDate
                    if (startDate != null && endDate != null) {
                        if ((day.owner == DayOwner.PREVIOUS_MONTH &&
                                    startDate.monthValue == day.date.monthValue &&
                                    endDate.monthValue != day.date.monthValue) ||
                            (day.owner == DayOwner.NEXT_MONTH &&
                                    startDate.monthValue != day.date.monthValue &&
                                    endDate.monthValue == day.date.monthValue) ||
                            (startDate < day.date && endDate > day.date &&
                                    startDate.monthValue != day.date.monthValue &&
                                    endDate.monthValue != day.date.monthValue)
                        ) {
                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middleblack)
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exTwoHeaderText
        }
        exTwoCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle =
                    "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }

        btn_next_claim.setOnClickListener {
            if (startDate == null && endDate == null) {
                Toast.makeText(context, "Выберите время", Toast.LENGTH_LONG).show()
            } else {
                serviceCreateModel?.apply {
                    startDate?.let { startDay = it.toString() }
                    endDate?.let { endDay = it.toString() }
                    serviceAddress?.let { address = it }
                }
                val bundle = bundleOf(
                    "serviceCreateModel" to Gson().toJson(serviceCreateModel),
                    "claimServiceModel" to Gson().toJson(claimServiceModel)
                )
                findNavController().navigate(R.id.action_claimthree_to_fourclaim, bundle)
            }
        }
    }

    private fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
        ContextCompat.getDrawable(this, drawable)

    private fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))


    fun makeRate() {
        startDate = null
        endDate = null
        exTwoCalendar.notifyCalendarChanged()
        txtch1.apply {
            background =
                ContextCompat.getDrawable(context, R.drawable.background_white_with_grey7_line)
            setTextColor(ContextCompat.getColor(context, R.color.grey_3))
        }
        txtch2.apply {
            background =
                ContextCompat.getDrawable(context, R.drawable.background_white_with_grey7_line)
            setTextColor(ContextCompat.getColor(context, R.color.grey_3))
        }
        txtch3.apply {
            background =
                ContextCompat.getDrawable(context, R.drawable.background_white_with_grey7_line)
            setTextColor(ContextCompat.getColor(context, R.color.grey_3))
            text = "Выбрать дату"
        }
        txtch4.apply {
            background =
                ContextCompat.getDrawable(context, R.drawable.background_white_with_grey7_line)
            setTextColor(ContextCompat.getColor(context, R.color.grey_3))
        }
        txtch5.apply {
            background =
                ContextCompat.getDrawable(context, R.drawable.background_white_with_grey7_line)
            setTextColor(ContextCompat.getColor(context, R.color.grey_3))
        }
        txtch6.apply {
            background =
                ContextCompat.getDrawable(context, R.drawable.background_white_with_grey7_line)
            setTextColor(ContextCompat.getColor(context, R.color.grey_3))
            text = "Выбрать дату"
        }
    }

    private fun setEditInitialViews(claimServiceModel: ServiceModel) {
        if (claimServiceModel.startDay != null && claimServiceModel.endDay != null) {
            editPeriodSelect(claimServiceModel.startDay ?: "", claimServiceModel.endDay ?: "")
        } else if (claimServiceModel.endDay != null) {
            editEndDaySelect(claimServiceModel.endDay ?: "")
        } else if (claimServiceModel.startDay != null) {
            editStartDaySelect(claimServiceModel.startDay ?: "")
        }
    }

    private fun editPeriodSelect(startDay: String, endDay: String) {
        spinner.setSelection(2)
        Handler().postDelayed({
            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateStart = startDay.split("-")
            calendar.set(dateStart[0].toInt(), dateStart[1].toInt() - 1, dateStart[2].toInt())
            startDate = LocalDate.parse(format.format(calendar.time))
            val dateEnd = endDay.split("-")
            calendar.set(dateEnd[0].toInt(), dateEnd[1].toInt() - 1, dateEnd[2].toInt())
            endDate = LocalDate.parse(format.format(calendar.time))
            setStartEndDateText()
        }, 500)
    }

    private fun editEndDaySelect(endDay: String) {
        spinner.setSelection(1)
        val calendar = Calendar.getInstance()
        val date = endDay.split("-")
        when (endDay) {
            LocalDate.now().toString() -> {
                txtch4.apply {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.background_grey_3_12dp_rectangle
                        )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            LocalDate.now().plusDays(1).toString() -> {
                txtch5.apply {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.background_grey_3_12dp_rectangle
                        )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            else -> {
                txtch6.apply {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.background_grey_3_12dp_rectangle
                        )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    text = date[2].plus("/").plus(date[1]).plus("/").plus(date[0])
                }
            }
        }
        calendar.set(date[0].toInt(), date[1].toInt(), date[2].toInt())
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        startDate = LocalDate.parse(format.format(calendar.time))
    }

    private fun editStartDaySelect(startDay: String) {
        spinner.setSelection(0)
        val calendar = Calendar.getInstance()
        val date = startDay.split("-")
        when (startDay) {
            LocalDate.now().toString() -> {
                txtch1.apply {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.background_grey_3_12dp_rectangle
                        )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            LocalDate.now().plusDays(1).toString() -> {
                txtch2.apply {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.background_grey_3_12dp_rectangle
                        )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            else -> {
                txtch3.apply {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.background_grey_3_12dp_rectangle
                        )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    text = date[2].plus("/").plus(date[1]).plus("/").plus(date[0])
                }
            }
        }
        calendar.set(date[0].toInt(), date[1].toInt(), date[2].toInt());
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        startDate = LocalDate.parse(format.format(calendar.time))
    }

    private fun setStartEndDateText() {
        if (startDate != null && endDate != null) {
            tvSelectedPeriod.text =
                "C\t".plus(startDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .plus("\tпо ").plus(endDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        } else if (startDate != null) {
            tvSelectedPeriod.text =
                "C\t".plus(startDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .plus("\tпо\t-\t")
        } else {
            tvSelectedPeriod.text = "C\t-\t".plus("по\t-\t")
        }
    }
}
