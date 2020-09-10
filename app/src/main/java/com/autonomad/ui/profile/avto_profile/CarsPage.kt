package com.autonomad.ui.profile.avto_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentProfileCarsBinding
import com.autonomad.ui.bottom_sheet.AddCarBottomDialog
import com.autonomad.utils.addOnItemTouchListener
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_profile_cars.*

class CarsPage : Fragment() {
    private val carsAdapter = CarsProfileAdapter()

    private val viewModel by viewModels<CarsPageViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentProfileCarsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        onClickListener()
        setupAdapter()
        viewModel.search()
    }

    private fun setupAdapter() {
        rv_profile_cars.apply {
            setHasFixedSize(true)
            adapter = carsAdapter
            addOnItemTouchListener { _, position ->
                findNavController().navigate(
                    R.id.action_profile_to_carsPageDetail,
                    bundleOf("idd" to carsAdapter.cars[position].id)
                )
            }
        }
    }

    private fun onClickListener() {
        add_profile_car.setOnClickListener {
//            val car = AddCarBottomDialogTwo(R.id.action_profile_to_carsPageDetail)
//            car.show(parentFragmentManager, "AddDriver")

            AddCarBottomDialog { viewModel.search() }.show(childFragmentManager, "AddCarFromCheckAutoDialog")

        }
        btn_profile_addcars.setOnClickListener {
            AddCarBottomDialog { viewModel.search() }.show(childFragmentManager, "AddCarFromCheckAutoDialog")
        }
    }

    private fun observer() {
        viewModel.checkAutoSearch.observe(viewLifecycleOwner, Observer {
            it.onSuccess { carsAdapter.cars = this }
            it.onFail(::tt)
        })
    }
}
