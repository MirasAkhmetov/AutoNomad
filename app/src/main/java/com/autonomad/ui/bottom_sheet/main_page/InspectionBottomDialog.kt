package com.autonomad.ui.bottom_sheet.main_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.data.models.main_page_car.Inspection
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.main_page.home.BottomDialogListener
import com.autonomad.ui.main_page.home.MainPageItem
import com.autonomad.utils.Status
import com.autonomad.utils.addOnItemTouchListener
import kotlinx.android.synthetic.main.bottom_sheet_main_page_widget.*

class InspectionBottomDialog(
    private val cars: LiveData<Status<List<GarageCar>>>,
    private val inspections: LiveData<Status<List<Inspection>>>,
    private val listener: BottomDialogListener
) : RoundBottomDialog() {

    private val inspectionAdapter = InspectionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_main_page_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { dismiss() }
        tv_title.text = "Срок Техосмотра"
        inspections.observe(viewLifecycleOwner) {
            it.onSuccess { inspectionAdapter.items = this }
        }
        cars.observe(viewLifecycleOwner) {
            it.onSuccess { inspectionAdapter.cars = this }
        }
        btn_new.setOnClickListener {
            listener.onNewClick(MainPageItem.INSPECTION)
            dismiss()
        }
        rv_items.apply {
            adapter = inspectionAdapter
            addOnItemTouchListener { _, position -> onClick(inspectionAdapter.cars[position].id) }
        }
    }

    private fun onClick(id: Int) {
        listener.onItemClick(MainPageItem.INSPECTION, id)
        dismiss()
    }
}
