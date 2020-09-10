package com.autonomad.ui.profile.driver_profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.databinding.FragmentProfileDriversBinding
import com.autonomad.ui.bottom_sheet.penalty.AddDriverBottomDialog
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.fragment_profile_drivers.*

class DriversPage : Fragment() {
    private val viewModel by viewModels<DriverPageViewModel>()
    private lateinit var driversPageAdapter: DriversPageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentProfileDriversBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        onClickListener()
        setupAdapter()
        viewModel.getDrivers(Methods.getToken())

    }

    private fun setupAdapter() {
        driversPageAdapter = DriversPageAdapter(viewModel, context as Context)
        rv_profile_cars.adapter = driversPageAdapter
    }

    private fun onClickListener() {
        btn_profile_adddrivers.setOnClickListener {
            AddDriverBottomDialog(R.id.action_profile_to_driverPageDetail).show(parentFragmentManager, "AddDriver")
        }
        add_profile_driver.setOnClickListener {
            AddDriverBottomDialog(R.id.action_profile_to_driverPageDetail).show(parentFragmentManager, "AddDriver")
        }

    }

    private fun observer() {
        viewModel.driver.observe(viewLifecycleOwner) {
            driversPageAdapter.driver = it
        }
    }
}
