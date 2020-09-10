package com.autonomad.ui.profile.driver_profile

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.autonomad.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_general_drivpage.*

class GeneralDrivPage(private val choose: Chosee, context: Context?) : BottomSheetDialog(context!!) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_general_drivpage)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null

        cancel.setOnClickListener {
            dismiss()
        }
        delete.setOnClickListener {
            choose.delete()
            dismiss()
//            val alert = AlertPenalty(context)
//            alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            alert.show()
        }

    }


    interface Chosee {
        fun delete()
        fun cancel()

    }
}