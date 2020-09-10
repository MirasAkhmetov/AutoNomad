package com.autonomad.ui.bottom_sheet.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.AddCarBottomDialog
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.utils.ScrollListener
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.bottom_sheet_insurance_cars_list.*

class CarsListFragment : RoundBottomDialog(), ScrollListener {
    private val viewModel by viewModels<InsuranceCarsViewModel>()
    private val adapter = CarAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_insurance_cars_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_cars.adapter = adapter
        viewModel.cars.observe(viewLifecycleOwner) {
            it.onSuccess {
                adapter.items = list
                adapter.checkedCar =
                    adapter.items.map { car -> car.stateNumber }.indexOf(arguments?.getString(CheckPolis.STATE_NUMBER))
                if (next == null) adapter.finish()
            }
            it.onFail(::tt)
        }
        btn_select.setOnClickListener {
            if (adapter.checkedCar == -1) toast("Выберите авто")
            else {
                val car = adapter.items[adapter.checkedCar]
                val args = (arguments ?: Bundle()).apply {
                    putString(CheckPolis.TITLE, car.titleFirst)
                    putString(CheckPolis.STATE_NUMBER, car.stateNumber)
                }
                CheckPolis.show(parentFragmentManager, args)
                dismiss()
            }
        }
        iv_back.setOnClickListener {
            CheckPolis.show(parentFragmentManager, arguments)
            dismiss()
        }
        tv_change.setOnClickListener {
            findNavController().navigate(R.id.action_global_profile)
        }
        tv_add_car.setOnClickListener {
            AddCarBottomDialog { viewModel.loadCars() }.show(parentFragmentManager, "AddCar")
        }
    }

    override fun loadMore() {
        viewModel.loadCars()
    }

    companion object {
        fun show(fragmentManager: FragmentManager, args: Bundle?) {
            CarsListFragment().apply { arguments = args }.show(fragmentManager, "CarList")
        }
    }
}
