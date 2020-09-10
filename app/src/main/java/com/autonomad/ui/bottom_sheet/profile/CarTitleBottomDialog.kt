package com.autonomad.ui.bottom_sheet.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import kotlinx.android.synthetic.main.bottom_sheet_profile_car_title.*

class CarTitleBottomDialog(private val onSaveClick: (String) -> Unit) : RoundBottomDialog() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setExpanded()
        return inflater.inflate(R.layout.bottom_sheet_profile_car_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        et_car_title.doAfterTextChanged {
            btn_save.isEnabled = !it.isNullOrEmpty()
        }
        btn_save.setOnClickListener {
            onSaveClick(et_car_title.text.toString())
            dismiss()
        }
        iv_clear.setOnClickListener { et_car_title.text.clear() }
    }
}