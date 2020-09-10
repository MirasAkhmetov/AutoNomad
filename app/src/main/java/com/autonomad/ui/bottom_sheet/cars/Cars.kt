package com.autonomad.ui.bottom_sheet.cars

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.parking.pageInfo
import com.autonomad.databinding.BottomSheetCarsBinding
import com.autonomad.ui.bottom_sheet.AddCarBottomDialog
import com.autonomad.ui.parking.home.HomeViewModel
import com.autonomad.utils.RecyclerItemClickListener
import com.autonomad.utils.RoundedBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_cars.*

class Cars(val viewmodel: HomeViewModel) : RoundedBottomSheetDialogFragment() {
    private lateinit var ViewModel: BottomSheetCarsBinding
    private lateinit var carsAdapter: CarsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = BottomSheetCarsBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@Cars)
                .get(CarsViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }

        dialog!!.setOnShowListener { dialog ->
            val d = dialog as? BottomSheetDialog
            val bottomSheetInternal = d?.findViewById<View>(R.id.design_bottom_sheet) ?: return@setOnShowListener
            BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        return ViewModel.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModel.viewmodel?.getCars()
    }

    override fun initialise() {
    }

    override fun setObservers() {
        ViewModel.viewmodel?.cars?.observe(viewLifecycleOwner, Observer {
            carsAdapter.updateRepoList(it)
        })
    }

    override fun setAdapter() {
        val viewModel = ViewModel.viewmodel
        if (viewModel != null) {
            cars_rv.layoutManager = LinearLayoutManager(context)
            carsAdapter = CarsAdapter(ViewModel.viewmodel!!)
            cars_rv.adapter = carsAdapter
            val data = viewmodel.pageInfo.value
            cars_rv.addOnItemTouchListener(
                RecyclerItemClickListener(cars_rv,
                    object :
                        RecyclerItemClickListener.OnItemClickListener {
                        @SuppressLint("ResourceType")
                        override fun onItemClick(view: View, position: Int) {
                            viewmodel.setData(
                                pageInfo(
                                    data?.pin!!,
                                    data.hours,
                                    data.min,
                                    data.bankingCard,
                                    ViewModel.viewmodel?.cars?.value?.get(position)?.state_number!!,
                                    data.price
                                )
                            )
                            dismiss()
                        }
                    })
            )
        }
    }

    override fun setOnClickListener() {
        add_car.setOnClickListener {
            AddCarBottomDialog {
                ViewModel.viewmodel?.getCars()
            }.show(childFragmentManager, "AddCarFromCheckAutoDialog")
        }
    }
}