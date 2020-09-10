package com.autonomad.ui.profile.avto_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.databinding.FragmentCarspageDetailBinding
import com.autonomad.ui.bottom_sheet.main_page.AddSrtsBottomDialog
import com.autonomad.ui.bottom_sheet.profile.CarTitleBottomDialog
import com.autonomad.utils.timber
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_carspage_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CarsPageDetail : Fragment() {
    private val carId by lazy { arguments?.getInt("idd") ?: 0 }
    private val viewModel: CarsPageDetailViewModel by viewModel { parametersOf(carId) }

    private lateinit var driversAdapter: DriversAdapter
    private var vin: String = ""

    private var showLoading = 0
        set(value) {
            field = value
            progress_bar.isVisible = value > 0
            container.isVisible = value == 0
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCarspageDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observe()
        setupAdapter()
    }

    private fun observe() {
        viewModel.drivers.observe(viewLifecycleOwner) {
            lbl_drivers.isVisible = it.item?.isNotEmpty() == true
            rv_drivers.isVisible = it.item?.isNotEmpty() == true
            it.onSuccess {
                if (isNotEmpty()) driversAdapter.garageDriver = this
            }
        }
        viewModel.car.observe(viewLifecycleOwner) {
            showLoading = if (it.isLoading) showLoading or 1 else showLoading and 10
            it.onSuccess {
                this@CarsPageDetail.vin = vin
                srtstext.isEnabled = srts.isEmpty()
            }
            it.onFail(::tt)
        }
        viewModel.update.observe(viewLifecycleOwner) {
            switchh.isEnabled = !it.isLoading
            it.onSuccess { switchh.isChecked = this }
            it.onFail(::tt)
        }
        viewModel.delete.observe(viewLifecycleOwner) {
            showLoading = if (it.isLoading) showLoading or 10 else showLoading and 1
            it.onSuccess {
                toast(getString(R.string.car_deleted))
                activity?.onBackPressed()
            }
            it.onFail { message ->
                toast("Произошла ошибка во время удаления авто")
                timber(message)
            }
        }
    }

    private fun setOnClickListeners() {
        ic_back.setOnClickListener { activity?.onBackPressed() }
        delete_driv.setOnClickListener {
            AlertDialog.Builder(requireContext()).setMessage(getString(R.string.confirm_auto_delete))
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да") { _, _ ->
                    viewModel.deleteCar()
                }.show()
        }
        switchh.setOnClickListener { viewModel.patchIsMain(switchh.isChecked) }
        srtstext.setOnClickListener {
            val car = viewModel.car.value?.item ?: return@setOnClickListener
            val markModel = "${car.mark.name} ${car.model.name}"
            AddSrtsBottomDialog(markModel) { viewModel.updateCarSrts(car.id.toInt(), it) }.show(childFragmentManager, "srts")
        }
        ic_setting.setOnClickListener { _ ->
            CarTitleBottomDialog { title ->
                val titleData = viewModel.setCarTitle(title)
                titleData.observe(viewLifecycleOwner) {
                    it.onFail(::tt)
                    if (it.isFailure || it.isSuccess) titleData.removeObservers(viewLifecycleOwner)
                }
            }.show(childFragmentManager, "SetCarTitle")
        }
        rel_history.setOnClickListener {
            val args = bundleOf("vin" to vin)
            findNavController().navigate(R.id.action_cars_pagedetail_to_report, args)
        }
    }

    private fun setupAdapter() {
        rv_drivers.layoutManager = LinearLayoutManager(context)
        rv_drivers.setHasFixedSize(true)
        driversAdapter = DriversAdapter()
        rv_drivers.adapter = driversAdapter
    }
}