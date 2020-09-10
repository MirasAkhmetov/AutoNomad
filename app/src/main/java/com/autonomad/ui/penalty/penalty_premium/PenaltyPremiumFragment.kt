package com.autonomad.ui.penalty.penalty_premium

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.penalty.DriverListBottomDialog
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_penalty_premium.*

class PenaltyPremiumFragment : Fragment(R.layout.fragment_penalty_premium) {

    private val viewModel by viewModels<PenaltyPremiumViewModel>()
    private val driverAdapter = DriverAdapter()

    private val rbs by lazy { listOf(rb_m3, rb_m6, rb_y1, rb_f) }
    private var isArrowAnimating = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateBack(R.id.action_penalty_premium_to_penalty)

        rbs.forEach {
            it.setOnCheckedChangeListener { button, isChecked ->
                if (isChecked) rbs.forEach { rb -> if (rb != button) rb.isChecked = false }
            }
        }

        listOf(tv_old_price_m3, tv_old_price_m6, tv_old_price_y1, tv_old_price_f).forEach {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        iv_arrow.setOnClickListener {
            if (!isArrowAnimating) {
                isArrowAnimating = true
                it.animate().rotationBy(if (layout_info.isVisible) -180f else 180f)
                    .withEndAction { isArrowAnimating = false }.start()
                layout_info.isVisible = !layout_info.isVisible
            }
        }
        driverAdapter.onDriverAmountChange = ::onDriverAmountChange
        onDriverAmountChange(false)
        rv_drivers.adapter = driverAdapter
    }

    private fun onDriverAmountChange(hasDrivers: Boolean) {
        btn_next.text = if (hasDrivers) "Приобрести" else "Выбрать водителя"
        if (!hasDrivers) btn_next.setOnClickListener {
            viewModel.loadDrivers()
            DriverListBottomDialog(viewModel.drivers) {

                val driver = viewModel.drivers.value?.item?.firstOrNull { d -> d.id == it }
                if (driver != null) driverAdapter.addDriver(driver)
            }.show(childFragmentManager, "DriverList")
        } else btn_next.setOnClickListener(null)
    }
}