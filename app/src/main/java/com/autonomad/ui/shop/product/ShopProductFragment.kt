package com.autonomad.ui.shop.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.utils.navigateBack


class ShopProductFragment : Fragment(R.layout.fragment_shop_product) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateBack(R.id.action_shop_product_to_shop_home) // todo or action_shop_products_to_shop_products_by_category
    }
}