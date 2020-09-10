package com.autonomad.ui.alert_penalty

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.autonomad.R
import kotlinx.android.synthetic.main.alert_penalty.*

class AlertPenalty(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.alert_penalty)
        linearLayout.setOnClickListener { dismiss() }
    }
}

