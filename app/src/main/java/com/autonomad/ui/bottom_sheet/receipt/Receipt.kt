package com.autonomad.ui.bottom_sheet.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.databinding.BottomSheetReceiptBinding
import com.autonomad.ui.bottom_sheet.RoundBottomDialog

class Receipt(val insurance: InsuranceHistory) : RoundBottomDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return BottomSheetReceiptBinding.inflate(LayoutInflater.from(context), container, false)
            .apply {
                insurance = this@Receipt.insurance
                lifecycleOwner = lifecycleOwner
            }.root

    }
}