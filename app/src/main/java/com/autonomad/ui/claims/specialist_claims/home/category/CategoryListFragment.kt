package com.autonomad.ui.claims.specialist_claims.home.category

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.addOnItemTouchListener
import com.autonomad.utils.timber
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_category_list.*

class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private val viewModel by viewModels<CategoryListViewModel> {
        CategoryListViewModelProvider(arguments?.getInt("categoryId") ?: 0)
    }

    private val subcategoryAdapter = SubcategoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener { activity?.onBackPressed() }
        setAdapter()
        observe()
    }

    private fun setAdapter() {
        rv_subcategories.apply {
            adapter = subcategoryAdapter
            addOnItemTouchListener { _, position ->
                findNavController().navigate(
                    R.id.action_categoryList_to_repairAuto, bundleOf(
                        "subcategoryId" to subcategoryAdapter.items[position].id,
                        "category" to subcategoryAdapter.items[position].name
                    )
                )
            }
        }
    }

    private fun observe() {
        viewModel.status.observe(viewLifecycleOwner) {
            progress_bar.isVisible = it.isLoading
            it.onSuccess {
                subcategoryAdapter.items = subcategories ?: emptyList()
                tv_category.text = name
            }
            it.onFail { message ->
                toast(message)
                timber(message)
            }
        }
    }
}
