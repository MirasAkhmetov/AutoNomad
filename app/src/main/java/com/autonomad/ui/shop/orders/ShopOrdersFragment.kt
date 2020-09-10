package com.autonomad.ui.shop.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_shop_orders.*

class ShopOrdersFragment: Fragment(R.layout.fragment_shop_orders) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateBack(R.id.action_shop_orders_to_shop_settings)
        back_icon.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}