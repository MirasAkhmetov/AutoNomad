package com.autonomad.ui.parking.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentParkingDetailReceiptBinding
import com.autonomad.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_parking_detail_receipt.*

class DetailReceipt : BaseFragment() {
    private lateinit var ViewModel: FragmentParkingDetailReceiptBinding
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel =
            FragmentParkingDetailReceiptBinding.inflate(inflater, container, false)
                .apply {
                    viewmodel = ViewModelProvider(this@DetailReceipt)
                        .get(DetailReceiptViewModel::class.java)
                    lifecycleOwner = viewLifecycleOwner
                }
        return ViewModel.root
    }

    override fun initialise() {
    }

    override fun setObservers() {
    }

    override fun setAdapter() {
    }

    override fun setOnClickListener() {
        ic_back.setOnClickListener { activity?.onBackPressed() }

        btn_to_main.setOnClickListener { findNavController().popBackStack(R.id.main_page, false) }

        val fromParkingPayedSuccess = arguments?.getBoolean("payedSuccess")
        if (fromParkingPayedSuccess != null && fromParkingPayedSuccess == true) {
            ic_back.visibility = View.GONE
            val callback = object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.main_page, false)
                }
            }
            activity?.onBackPressedDispatcher?.addCallback(this, callback)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        id = arguments?.getString("id").toString()
        ViewModel.viewmodel?.getData(id)
        super.onViewCreated(view, savedInstanceState)
    }
}