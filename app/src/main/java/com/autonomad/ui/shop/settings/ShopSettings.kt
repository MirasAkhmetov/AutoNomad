package com.autonomad.ui.shop.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_shop_settings.*

class ShopSettings : Fragment(R.layout.fragment_shop_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateBack(R.id.action_shop_settings_to_shop_home)

        fav.setOnClickListener {
            findNavController().navigate(R.id.action_shop_settings_to_shop_fav_products)
        }
        zakaz.setOnClickListener {
            findNavController().navigate(R.id.action_shop_settings_to_shop_orders)
        }
        support.setOnClickListener {
            findNavController().navigate(R.id.action_shop_settings_to_claim_specialist_contacts)
        }
    }
}