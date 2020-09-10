package com.autonomad.ui.bottom_sheet.banking_cards

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.autonomad.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class BankingCardAdd(context: Context) : BottomSheetDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_add_banking_card)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null
    }
}