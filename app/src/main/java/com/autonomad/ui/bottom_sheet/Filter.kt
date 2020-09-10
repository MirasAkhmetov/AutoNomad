package com.autonomad.ui.bottom_sheet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.autonomad.R
import com.autonomad.data.models.parking.ParkingFilter
import com.autonomad.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_history_filter.*

class Filter(
    context: Context,
    private val defaultFilterType: Int?,
    private val onApplyClick: (ParkingFilter?) -> Unit
) :
    BottomSheetDialog(context) {
    private var selectedFilterPeriodType: Int? = null
    private var selectedFilter: ParkingFilter? = null
    private val dates = ArrayList<ParkingFilter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_history_filter)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null
        setAdapter()

        tv_reset_all.setOnClickListener {
            rg_filter.check(-1)
            selectedFilterPeriodType = null
            selectedFilter = null
        }
        btn_apply.setOnClickListener {
            onApplyClick(selectedFilter)
            dismiss()
        }
        ib_close.setOnClickListener { dismiss() }
        rg_filter.setOnCheckedChangeListener { group, checkedId ->
            selectedFilterPeriodType = try {
                group.findViewById<RadioButton>(checkedId).tag as Int
            } catch (ex: Exception) {
                null
            }
            selectedFilter = try {
                dates.firstOrNull { group.findViewById<RadioButton>(checkedId).tag == it.id }
            } catch (ex: Exception) {
                null
            }
        }
    }

    private fun setAdapter() {
        dates.add(ParkingFilter(1, "За неделю"))
        dates.add(ParkingFilter(2, "За месяц"))
        dates.add(ParkingFilter(3, "За 3 месяца"))
        dates.add(ParkingFilter(4, "За полгода"))
        dates.add(ParkingFilter(5, "За год"))

        dates.forEach { parkingFilterModel ->
            val rb_filter = RadioButton(context).apply {
                buttonDrawable = ContextCompat.getDrawable(context, R.drawable.checkbox_selector)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(48, context)
                )
                    .apply { marginStart = dpToPx(16, context); marginEnd = dpToPx(16, context); }
                text = parkingFilterModel.name
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                tag = parkingFilterModel.id
                id = parkingFilterModel.id
                TextViewCompat.setTextAppearance(this, R.style.FilterRadioButtonTextViewStyle)
            }
            rg_filter.addView(rb_filter)
        }
        defaultFilterType?.let {
            rg_filter.check(it)
        }
    }
}