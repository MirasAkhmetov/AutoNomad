package com.autonomad.ui.shop.fav

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_shop_fav_products.*

class ShopFavProductsFragment : Fragment(R.layout.fragment_shop_fav_products) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateBack(R.id.action_shop_fav_products_to_shop_settings)
        back_icon.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}