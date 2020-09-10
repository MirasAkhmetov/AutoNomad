package com.autonomad.ui.penalty.penalty_home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.penalty.AddDriverBottomDialog
import com.autonomad.ui.bottom_sheet.penalty.DriverOptionsDialog
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_penalty_home.*

class PenaltyHomeFragment : Fragment(R.layout.fragment_penalty_home) {
    private val driverAdapter = DriverAdapter(::onDelete, ::onRefresh)
    private val viewModel by viewModels<PenaltyHomeViewModel>()
    private val sampleImages = intArrayOf(R.drawable.ic_banner1, R.drawable.ic_banner2, R.drawable.ic_banner3)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        initialise()
        onClickListener()
        setupAdapter()
        viewModel.loadDrivers()
    }

    private fun setupAdapter() {
        drivers_rv.adapter = driverAdapter
    }

    private fun onClickListener() {
        add.setOnClickListener {
            val car = AddDriverBottomDialog(R.id.action_penalty_to_penaltiesOfPerson)
            car.show(parentFragmentManager, "AddDriver")
        }
    }

    private fun observe() {
        viewModel.drivers.observe(viewLifecycleOwner) {
            it.onSuccess { driverAdapter.drivers = this }
            if (it.isLoading && progress_bar.isVisible) progress_bar.isVisible = false
        }
    }


    private fun initialise() {
        navigateBack(R.id.action_global_services)
        carouselView.setImageListener { position, imageView ->
            imageView.setImageResource(sampleImages[position])
            imageView.adjustViewBounds = true
        }
        carouselView.pageCount = sampleImages.size
    }

    private fun onDelete(id: Int) {
        DriverOptionsDialog(requireContext()) {
            AlertDialog.Builder(requireContext()).setTitle("Удалить выбранного водителя?").setNegativeButton("Нет", null)
                .setPositiveButton("Да") { _, _ ->
                    viewModel.deleteDriver(id)
                }.show()
        }.apply {
            findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
            show()
        }
    }

    private fun onRefresh() {
        viewModel.loadDrivers()
    }
}