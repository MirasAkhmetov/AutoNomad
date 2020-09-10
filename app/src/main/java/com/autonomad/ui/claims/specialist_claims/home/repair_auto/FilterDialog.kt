package com.autonomad.ui.claims.specialist_claims.home.repair_auto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import kotlinx.android.synthetic.main.bottom_sheet_claim_filter.*

class FilterDialog(private var mFilter: Int = NO_FILTER, private val onApplyClick: (Int) -> Unit) : RoundBottomDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setExpanded()
        return inflater.inflate(R.layout.bottom_sheet_claim_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_close.setOnClickListener {
            dismiss()
        }
        if (mFilter != NO_FILTER) radio_group.check(radio_group.getChildAt(mFilter).id)
        radio_group.setOnCheckedChangeListener { group, checkedId ->
            mFilter = group.indexOfChild(group.findViewById(checkedId))
        }

        btn_apply.setOnClickListener {
            onApplyClick(mFilter)
            dismiss()
        }
    }

    companion object {
        const val NO_FILTER = -1
        const val NEW = 0
        const val PRICE_INC = 1
        const val PRICE_DEC = 2
        const val NEAR = 3
    }
}