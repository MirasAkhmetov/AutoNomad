package com.autonomad.ui.bottom_sheet.main_page

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.utils.Status
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.bottom_sheet_add_srts.*
import java.util.regex.Pattern

class AddSrtsBottomDialog(private val carModel: String, private val onClick: (String) -> LiveData<Status<Int>>) :
    RoundBottomDialog() {

    private val pattern = Pattern.compile("^[A-Z]{2}[0-9]{8}")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setExpanded()
        return inflater.inflate(R.layout.bottom_sheet_add_srts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { dismiss() }
        tv_car_info.text = carModel
        et_srts.filters = arrayOf(InputFilter.AllCaps())
        et_srts.doAfterTextChanged { et ->
            val srts = et?.toString() ?: return@doAfterTextChanged
            btn_check.isEnabled = srts.length > 8
            if (pattern.matcher(srts).matches()) search(srts)
        }
        btn_check.setOnClickListener {
            search(et_srts.text.toString())
        }
    }

    private fun search(srts: String) {
        btn_check.isEnabled = false
        et_srts.isEnabled = false
        btn_check.text = ""
        progress_bar.isVisible = true
        onClick(srts).observe(viewLifecycleOwner) {
            it.onSuccess { dismiss() }
            it.onFail { message ->
                et_srts.isEnabled = true
                btn_check.isEnabled = true
                btn_check.text = getString(R.string.check)
                progress_bar.isVisible = false
                tt(message)
            }
        }
    }
}