package com.autonomad.ui.bottom_sheet.claim

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.autonomad.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_claims_specialist_response_properties.*

class ResponseGeneralProperties(
    context: Context,
    private val type: Int,
    private val listener: ResponseOptions
) : BottomSheetDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_claims_specialist_response_properties)
        val frame = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        frame?.background = null
        tv_delete_response.isVisible = type and SHOW_DELETE != 0
        tv_delete_response.setOnClickListener { listener.delete() }
        tv_finish_response.isVisible = type and SHOW_FINISH != 0
        tv_finish_response.setOnClickListener { listener.finish() }
        tv_show_review.isVisible = type and SHOW_REVIEW != 0
        tv_show_review.setOnClickListener { listener.showReview() }
    }

    companion object {
        const val CREATED = 2
        const val ACTIVE = 6
        const val COMPLETED = 1

        private const val SHOW_DELETE = 2
        private const val SHOW_FINISH = 4
        private const val SHOW_REVIEW = 1
    }
}

interface ResponseOptions {
    fun delete()
    fun finish()
    fun showReview()
}
