package com.autonomad.ui.bottom_sheet.main_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.main_page.home.BottomDialogListener
import com.autonomad.ui.main_page.home.MainPageItem
import com.autonomad.utils.Status
import com.autonomad.utils.addOnItemTouchListener
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.bottom_sheet_main_page_widget.*

class InsuranceBottomDialog(
    private val insurance: LiveData<Status<List<InsuranceHistory>>>,
    private val listener: BottomDialogListener
) : RoundBottomDialog() {

    private val insuranceAdapter = InsuranceAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setExpanded()
        return inflater.inflate(R.layout.bottom_sheet_main_page_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { dismiss() }
        tv_title.text = "Срок страховки"
        btn_new.text = "Новый полис"
        btn_new.setOnClickListener {
            listener.onNewClick(MainPageItem.INSURANCE)
            dismiss()
        }
        rv_items.apply {
            adapter = insuranceAdapter
            addOnItemTouchListener { _, position ->
                listener.onItemClick(MainPageItem.INSURANCE, insuranceAdapter.items[position].id)
                dismiss()
            }
        }
        insurance.observe(viewLifecycleOwner) {
            it.onSuccess { insuranceAdapter.items = this }
            it.onFail(::tt)
        }
    }
}