package com.autonomad.ui.bottom_sheet.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.bottom_sheet.insurance.CarAdapter
import com.autonomad.ui.bottom_sheet.insurance.InsuranceCarsViewModel
import com.autonomad.utils.OnItemIdSelected
import com.autonomad.utils.ScrollListener
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.bottom_sheet_insurance_cars_list.*

class CarListBottomDialog(private val onCarSelected: OnItemIdSelected) : RoundBottomDialog(), ScrollListener {

    private val viewModel by viewModels<InsuranceCarsViewModel>()
    private val adapter = CarAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setExpanded()
        return inflater.inflate(R.layout.bottom_sheet_insurance_cars_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_cars.adapter = adapter
        tv_change.isVisible = false
        tv_add_car.isVisible = false
        viewModel.cars.observe(viewLifecycleOwner) {
            it.onSuccess {
                adapter.items = list
                if (next == null) adapter.finish()
            }
            it.onFail(::tt)
        }
        btn_select.setOnClickListener {
            if (adapter.checkedCar == -1) toast("Выберите авто")
            else {
                val car = adapter.items[adapter.checkedCar]
                onCarSelected(car.id)
                dismiss()
            }
        }
    }

    override fun loadMore() {
        viewModel.loadCars()
    }
}