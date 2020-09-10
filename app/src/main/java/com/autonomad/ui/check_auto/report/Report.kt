package com.autonomad.ui.check_auto.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.check_auto.CreateOrder
import com.autonomad.databinding.FragmentCheckAutoReportBinding
import com.autonomad.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_check_auto_report.*
import org.koin.android.viewmodel.ext.android.viewModel

class Report : BaseFragment() {
    private val viewModel: ReportViewModel by viewModel()
    private val vin by lazy { arguments?.getString("vin") }
    private val orderId by lazy { arguments?.getInt("orderId") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCheckAutoReportBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun initialise() {
        viewModel.loadData(vin, orderId)
    }

    override fun setOnClickListener() {
        if (checkbox1.isChecked || checkbox2.isChecked) {
            check.setOnClickListener {
                viewModel.createOrder(
                    CreateOrder(
                        vin.orEmpty(),
                        includeDtp = checkbox1.isChecked,
                        includeHistory = checkbox2.isChecked
                    )
                )
            }
        } else {
            check.setOnClickListener onCheckClick@{
                activity?.onBackPressed()
            }
        }
        ic_back.setOnClickListener { activity?.onBackPressed() }

        val checkboxChangeListener: (CompoundButton, Boolean) -> Unit = { _, _ ->
            check.text =
                if (checkbox1.isChecked || checkbox2.isChecked) "Купить отчет" else "Готово"
            if (checkbox1.isChecked || checkbox2.isChecked) {
                check.setOnClickListener {
                    viewModel.createOrder(
                        CreateOrder(
                            vin.orEmpty(),
                            includeDtp = checkbox1.isChecked,
                            includeHistory = checkbox2.isChecked
                        )
                    )
                }
            } else {
                check.setOnClickListener onCheckClick@{
                    activity?.onBackPressed()
                }
            }
        }
        checkbox1.setOnCheckedChangeListener(checkboxChangeListener)
        checkbox2.setOnCheckedChangeListener(checkboxChangeListener)
        checkbox_ll1.setOnClickListener {
            checkbox1.isChecked = !checkbox1.isChecked
        }
        checkbox_ll2.setOnClickListener {
            checkbox2.isChecked = !checkbox2.isChecked
        }
    }

    override fun setObservers() {
        viewModel.order.observe(viewLifecycleOwner, Observer {
            if (it.orderId.isNullOrEmpty()) viewModel.checkStatus(it.id)
            else if (it.orderId != "changed") {
                val bundle = bundleOf("id" to it.id, "order_id" to it.orderId, "price" to it.price)
                viewModel.order.value?.orderId = "changed"
                findNavController().navigate(R.id.action_report_to_payment, bundle)
            }
        })

        viewModel.detailTicket.observe(viewLifecycleOwner, Observer {
            if (it.report != null) {
                rv_dtps.adapter = DtpAdapter(it.report.car_dtps)
                rv_histories.adapter = HistoriesAdapter(it.report.car_histories)
            }
        })
    }

    override fun setAdapter() {}
}