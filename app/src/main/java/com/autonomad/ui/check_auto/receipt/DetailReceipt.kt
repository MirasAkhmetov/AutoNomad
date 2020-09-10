package com.autonomad.ui.check_auto.receipt

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.navigateBack
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_check_auto_detail_receipt.*
import kotlinx.android.synthetic.main.fragment_check_auto_detail_receipt.main_content
import kotlinx.android.synthetic.main.fragment_check_auto_detail_receipt.progress_bar
import org.koin.android.viewmodel.ext.android.viewModel

class DetailReceipt : Fragment(R.layout.fragment_check_auto_detail_receipt) {
    private val detailReceiptViewModel: DetailReceiptViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navigateBack(R.id.action_detailTicket_to_check_auto_history)
        ic_back.setOnClickListener { activity?.onBackPressed() }

        detailReceiptViewModel.detailsTicket.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (!it.updated_at.isNullOrEmpty())
                    date.text = it.updated_at.replace("T", "\n").substringBefore(".")
                if (!it.order_id.isNullOrEmpty())
                    orderId.text = it.order_id
                if (it.report != null && it.report.car_info != null && !it.report.car_info.vin_number.isNullOrEmpty()) {
                    vin.text = it.report.car_info.vin_number
                }
                if (!it.price.isNullOrEmpty()) {
                    sum.text = it.price
                }
                if (it.user != null) {
                    fullname.text = "${it.user.last_name} ${it.user.first_name}"
                }
                if (it.card != null) {
                    cardNumber.text = "****${it.card.last_four}"
                }
            }
        })
        detailReceiptViewModel.toast.observe(viewLifecycleOwner, Observer {
            toast(it)
        })
        detailReceiptViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_bar.visibility = View.VISIBLE
                main_content.visibility = View.GONE
            } else {
                progress_bar.visibility = View.GONE
                main_content.visibility = View.VISIBLE
            }
        })


        val orderId = arguments?.getInt("id") ?: 0
        btn_show.setOnClickListener {
            val bundle = bundleOf("orderId" to orderId)
            findNavController().navigate(R.id.action_detailTicket_to_check_auto_report, bundle)
        }
        detailReceiptViewModel.getData(orderId)
    }
}