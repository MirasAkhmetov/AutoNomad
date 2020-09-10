package com.autonomad.ui.bottom_sheet.main_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.penalty.Driver
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.main_page.home.BottomDialogListener
import com.autonomad.ui.main_page.home.MainPageItem
import com.autonomad.utils.Status
import com.autonomad.utils.addOnItemTouchListener
import kotlinx.android.synthetic.main.bottom_sheet_main_page_widget.*

class PenaltiesBottomDialog(
    private val drivers: LiveData<Status<List<Driver>>>,
    private val listener: BottomDialogListener
) :
    RoundBottomDialog() {

    private val TAG = "PenaltiesDialogLogcat"
    private val penaltiesAdapter = PenaltiesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_main_page_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { dismiss() }
        tv_title.text = "Наличие штрафов"
        btn_new.setOnClickListener {
            listener.onNewClick(MainPageItem.PENALTIES)
            dismiss()
        }
        rv_items.apply {
            adapter = penaltiesAdapter
            addOnItemTouchListener { _, position ->
                listener.onItemClick(MainPageItem.PENALTIES, penaltiesAdapter.drivers[position].id)
                dismiss()
            }
        }
        drivers.observe(viewLifecycleOwner) {
            it.onSuccess { penaltiesAdapter.drivers = this }
            it.onFailure {
                Log.e(TAG, "onViewCreated: $this")
            }
        }
    }
}