package com.autonomad.ui.shop.home

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.ui.services.ShopCitiesDialogFragment
import com.autonomad.ui.services.ShopCityViewModel
import com.autonomad.ui.shop.ProductGridListAdapter
import com.autonomad.ui.shop.model.PopularCategory
import com.autonomad.ui.shop.model.Product
import com.autonomad.utils.navigateBack
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_shop_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class ShopHomeFragment : Fragment(R.layout.fragment_shop_home) {

    private val viewModel: ShopHomeViewModel by viewModel()
    private val cityViewModel by activityViewModels<ShopCityViewModel>()

    //todo other banners
    private var sliders = intArrayOf(
        R.drawable.shop_banner1
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observeLiveData()

        viewModel.getPopularCategory()
        viewModel.getProducts()
    }

    private fun initUI() {
        navigateBack(R.id.action_shop_home_to_services)

        carouselView.apply {
            setImageListener { position, imageView ->
                imageView.setImageResource(sliders[position])
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            }
            pageCount = sliders.size
        }
        all_category.setOnClickListener {
            findNavController().navigate(R.id.action_shop_home_to_shop_category)
        }
        search_bar.setOnClickListener {
            findNavController().navigate(R.id.action_shop_home_to_shop_category)
        }
        tv_city.setOnClickListener {
            ShopCitiesDialogFragment().show(childFragmentManager, "ShopCitiesDialogFragment")
        }
        notFound.setOnClickListener {
            findNavController().navigate(R.id.action_shop_home_to_claim_specialist_contacts)
        }
    }

    private fun observeLiveData() {
        viewModel.toast.observe(viewLifecycleOwner, Observer {
            toast(it)
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                productList.visibility = GONE
                progress_bar.visibility = VISIBLE
            } else {
                productList.visibility = VISIBLE
                progress_bar.visibility = GONE
            }
        })
        viewModel.popularCategory.observe(viewLifecycleOwner, Observer {
            val adapter = PopularCategoryListAdapter(requireContext(), it, ::onCategoryClicked)
            categoryList.adapter = adapter
            categoryList.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        })

        viewModel.products.observe(viewLifecycleOwner, Observer {
            val adapter = ProductGridListAdapter(
                requireContext(),
                it,
                ::onProductClicked,
                ::addToFavourite,
                ::removeFromFavourite
            )
            productList.adapter = adapter
            productList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        })
        cityViewModel.user.observe(viewLifecycleOwner) {
            tv_city.text = it.item?.city?.name ?: getString(R.string.select_city)
        }
    }

    private fun onCategoryClicked(item: PopularCategory) {
        findNavController().navigate(R.id.action_shop_home_to_shop_category)
    }

    private fun onProductClicked(item: Product) {
        findNavController().navigate(R.id.action_shop_home_to_shop_products)
    }

    private fun addToFavourite(item: Product) {

    }

    private fun removeFromFavourite(item: Product) {

    }
}