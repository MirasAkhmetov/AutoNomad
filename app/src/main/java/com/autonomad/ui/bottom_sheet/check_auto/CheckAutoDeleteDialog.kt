package com.autonomad.ui.bottom_sheet.check_auto

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.autonomad.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_checkauto_properties.*

class CheckAutoDeleteDialog(private val action: CheckAutoAction, context: Context) :
    BottomSheetDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_checkauto_properties)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null

        delete.setOnClickListener {
            action.deleteClicked()
            dismiss()
        }
    }

    interface CheckAutoAction {
        fun deleteClicked()
    }
}