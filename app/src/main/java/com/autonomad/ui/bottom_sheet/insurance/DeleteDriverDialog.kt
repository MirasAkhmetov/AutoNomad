package com.autonomad.ui.bottom_sheet.insurance

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.autonomad.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_insurance_driver_properties.*

class DeleteDriverDialog(private val action: DeleteDriverAction, context: Context) :
    BottomSheetDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_insurance_driver_properties)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null

        delete.setOnClickListener {
            action.deleteClicked()
            dismiss()
        }
    }

    interface DeleteDriverAction {
        fun deleteClicked()
    }
}