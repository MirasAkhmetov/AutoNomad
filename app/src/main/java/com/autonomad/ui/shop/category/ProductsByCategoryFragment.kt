package com.autonomad.ui.shop.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.utils.navigateBack

class ProductsByCategoryFragment : Fragment(
    R.layout.fragment_shop_products_by_category
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateBack(R.id.action_shop_products_by_category_to_shop_category)

    }
}