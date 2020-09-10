package com.autonomad.ui.bottom_sheet.penalty

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.autonomad.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_driver_properties.*

class DriverOptionsDialog(context: Context, private val onDelete: () -> Unit) : BottomSheetDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_driver_properties)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null

        tv_delete.setOnClickListener {
            onDelete()
            dismiss()
        }
    }
}
