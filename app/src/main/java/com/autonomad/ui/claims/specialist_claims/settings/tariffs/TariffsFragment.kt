package com.autonomad.ui.claims.specialist_claims.settings.tariffs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.claims.Tariff
import com.autonomad.ui.bottom_sheet.claim.PaymentBottomDialog
import com.autonomad.utils.addOnItemTouchListener
import com.autonomad.utils.onBackPress
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_claims_specialist_tariffs.*

class TariffsFragment : Fragment(R.layout.fragment_claims_specialist_tariffs) {

    private val viewModel by viewModels<TariffsViewModel>()
    private val tariffsAdapter = TariffsAdapter()
    private var isAnimating = false
    private var selectedTariff: Tariff? = null
        set(value) {
            field = value
            rv_tariffs.isVisible = value == null
            card_not_active.isVisible = value == null
            tv_selected_tariff.isVisible = value != null
            if (value != null) tv_selected_tariff.text = getString(R.string.tariff_name, value.name, value.count)
            btn_next.text = if (value == null) "" else getString(R.string.payment_amount, value.price.substringBefore("."))
            btn_next.isVisible = value != null
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setOnClickListeners()

        viewModel.tariffs.observe(viewLifecycleOwner) {
            pb_tariffs.isVisible = it.isLoading
            it.onSuccess { tariffsAdapter.items = list }
            it.onFail(::tt)
        }
        viewModel.order.observe(viewLifecycleOwner) {
            btn_next.isEnabled = !it.isLoading
            progress_bar.isVisible = it.isLoading
            it.onSuccess {
                if (orderId != null) PaymentBottomDialog.show(childFragmentManager, orderId, price)
                else toast("Произошла ошибка при получении заказа")
            }
            it.onFail(::tt)
        }
    }

    private fun setOnClickListeners() {
        iv_arrow.setOnClickListener {
            if (isAnimating) return@setOnClickListener
            isAnimating = true
            it.animate().rotationBy(if (group_hideable.isVisible) -180f else 180f).withEndAction { isAnimating = false }
                .start()
            group_hideable.isVisible = !group_hideable.isVisible
        }

        btn_next.setOnClickListener {
            selectedTariff?.let { tariff ->
                viewModel.orderTariff(tariff.id)
            }
        }
        iv_back.setOnClickListener { activity?.onBackPressed() }
    }

    private fun initialize() {
        rv_tariffs.apply {
            adapter = tariffsAdapter
            addOnItemTouchListener { _, position ->
                selectedTariff = tariffsAdapter.items[position]
            }
        }

        onBackPress {
            if (selectedTariff != null) selectedTariff = null
            else findNavController().popBackStack()
        }
    }
}