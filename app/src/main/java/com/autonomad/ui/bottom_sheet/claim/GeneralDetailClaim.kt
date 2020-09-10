package com.autonomad.ui.bottom_sheet.claim

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.autonomad.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_general_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_general_properties.delete
import kotlinx.android.synthetic.main.bottom_sheet_general_properties.edit

class GeneralDetailClaim(private val choose: Chosee, context: Context?) : BottomSheetDialog(context!!) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_general_detail)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null

        share.setOnClickListener {
            choose.share()
            dismiss()
        }
        delete.setOnClickListener {
            choose.delete()
            dismiss()
        }
        edit.setOnClickListener {
            choose.edit()
            dismiss()
        }

    }

    interface Chosee {
        fun delete()
        fun edit()
        fun share()

    }
}