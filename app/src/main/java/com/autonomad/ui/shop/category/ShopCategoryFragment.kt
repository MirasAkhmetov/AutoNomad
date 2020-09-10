package com.autonomad.ui.shop.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_shop_category.*

class ShopCategoryFragment : Fragment(R.layout.fragment_shop_category) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateBack(R.id.action_shop_category_to_shop_home)



    }


    private fun showProducts() {
        findNavController().navigate(R.id.action_shop_category_to_shop_products_by_category)
    }
}