package com.autonomad.ui.shop.basket

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.utils.navigateBack

class ShopBasketFragment : Fragment(R.layout.fragment_shop_basket) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateBack(R.id.action_shop_basket_to_shop_home)
    }
}