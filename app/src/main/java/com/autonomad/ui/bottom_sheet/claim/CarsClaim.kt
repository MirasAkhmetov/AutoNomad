package com.autonomad.ui.bottom_sheet.claim

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
import com.autonomad.data.models.claim_user.createInfo
import com.autonomad.databinding.BottomSheetCarsclaimBinding
import com.autonomad.ui.bottom_sheet.cars.CarsViewModel
import com.autonomad.ui.claims.user_claims.claims.CreateeViewModel
import com.autonomad.utils.RecyclerItemClickListener
import com.autonomad.utils.RoundedBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_cars.*

class CarsClaim(val viewmodel: CreateeViewModel) : RoundedBottomSheetDialogFragment() {
    private lateinit var ViewModel: BottomSheetCarsclaimBinding
    private lateinit var carsAdapter: CarsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = BottomSheetCarsclaimBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@CarsClaim)
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
            //val data = viewmodel.createInfo.value
            cars_rv.addOnItemTouchListener(
                RecyclerItemClickListener(cars_rv,
                    object :
                        RecyclerItemClickListener.OnItemClickListener {
                        @SuppressLint("ResourceType")
                        override fun onItemClick(view: View, position: Int) {
                            Log.d("asdfg2", ViewModel.viewmodel?.cars?.value?.get(position)?.state_number!!)
                            viewmodel.setData(
                                createInfo(
                                    ViewModel.viewmodel?.cars?.value?.get(position)?.state_number!!,
                                    ViewModel.viewmodel?.cars?.value?.get(position)?.title!!
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
            dismiss()
            //Home.showBottomSheetAddCar()
        }
    }
}