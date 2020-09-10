package com.autonomad.ui.claims.user_claims.home.mycategory

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.autonomad.R
import com.autonomad.data.models.claims.MasterFilter
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.utils.dpToPx
import kotlinx.android.synthetic.main.bottom_sheet_claim_filter.*

class FilterDialog(private var mFilter: Int?, private val onApplyClick: (MasterFilter?) -> Unit) :
    RoundBottomDialog() {

    private val filterList: ArrayList<MasterFilter> = ArrayList()
    private var selectedFilter: MasterFilter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setExpanded()
        return inflater.inflate(R.layout.bottom_sheet_claim_filter, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        radio_group.setOnCheckedChangeListener { group, checkedId ->
            selectedFilter = try {
                filterList.firstOrNull { group.findViewById<RadioButton>(checkedId).tag == it.id }
            } catch (ex: Exception) {
                null
            }
        }

        iv_close.setOnClickListener {
            dismiss()
        }
        btn_apply.setOnClickListener {
            onApplyClick(selectedFilter)
            dismiss()
        }

        fillFilterData()

        filterList.forEach { masterFilter ->
            val rbFilter = RadioButton(context).apply {
                buttonDrawable = ContextCompat.getDrawable(context, R.drawable.selector_filter)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(48, context)
                )
                    .apply { marginStart = dpToPx(16, context); marginEnd = dpToPx(16, context); }
                text = masterFilter.name
                setTextColor(ContextCompat.getColor(context, R.color.grey_2))
                textSize = 16f
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                gravity = Gravity.FILL_HORIZONTAL and Gravity.CENTER_VERTICAL
                tag = masterFilter.id
                id = masterFilter.id
                TextViewCompat.setTextAppearance(this, R.style.FilterRadioButtonTextViewStyle)

            }
            radio_group.addView(rbFilter)
        }
        mFilter?.let {
            radio_group.check(it)
        }
    }

    private fun fillFilterData() {
        filterList.add(MasterFilter(100,getString(R.string.sort_by_rating_ascending), MasterFilter.Type.RATING,false))
        filterList.add(MasterFilter(101,getString(R.string.sort_by_rating_descending), MasterFilter.Type.RATING,true))
        filterList.add(MasterFilter(102,getString(R.string.sort_by_popularity_ascending), MasterFilter.Type.POPULARITY,false))
        filterList.add(MasterFilter(103,getString(R.string.sort_by_popularity_descending), MasterFilter.Type.POPULARITY,true))
    }
}