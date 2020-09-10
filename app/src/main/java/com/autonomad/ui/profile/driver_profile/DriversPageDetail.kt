package com.autonomad.ui.profile.driver_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentDriverspageDetailBinding
import com.autonomad.ui.bottom_sheet.profile.CarListBottomDialog
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_driverspage_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DriversPageDetail : Fragment() {
    private val uinArg by lazy { arguments?.getString("UIN") }
    private val viewModel: DriverPageDetailViewModel by viewModel {
        parametersOf(
            arguments?.getInt("id") ?: 0,
            uinArg.orEmpty()
        )
    }
    private val carsAdapter = CarsAdapter(::deleteCar)

    private var uin = ""
    private var driverId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentDriverspageDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener { activity?.onBackPressed() }

        viewModel.loadData()

        rel_delet_driv.setOnClickListener {
            AlertDialog.Builder(requireContext()).setMessage(getString(R.string.confirm_driver_deletion))
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да") { _, _ ->
                    viewModel.deleteDriver().observe(viewLifecycleOwner) {
                        if (it == true) {
                            toast("Водитель удален")
                            activity?.onBackPressed()
                        }
                    }
                }.show()
        }

        rel_penalty.setOnClickListener {
            val args = bundleOf("UIN" to (uin))
            findNavController().navigate(R.id.action_drivpage_to_penaltiesOfPerson, args)
        }
        tv_add_car.setOnClickListener { _ ->
            CarListBottomDialog { id ->
                viewModel.setCar(id, true).observe(viewLifecycleOwner) {
                    if (it) {
                        toast(getString(R.string.car_added))
                        viewModel.loadData()
                    }
                }
            }.show(childFragmentManager, "CarList")
        }

        recyclerviewka.adapter = carsAdapter
        observer()
    }

    private fun observer() {
        viewModel.cars.observe(viewLifecycleOwner) {
            carsAdapter.garageCars = it
        }
        viewModel.driver.observe(viewLifecycleOwner) {
            driverId = it.id
            uin = it.target
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it == "invalidData") {
                toast("Переданы неверные данные")
                activity?.onBackPressed()
            }
            toast(it)
        }
    }

    private fun deleteCar(carId: Int) {
        viewModel.setCar(carId, false).observe(viewLifecycleOwner) {
            carsAdapter.notifyDataSetChanged()
            if (it) {
                toast(getString(R.string.car_deleted))
                viewModel.loadData()
            }
        }
    }
}