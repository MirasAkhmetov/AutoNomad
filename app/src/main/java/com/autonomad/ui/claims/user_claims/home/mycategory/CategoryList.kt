package com.autonomad.ui.claims.user_claims.home.mycategory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentCategoryListSpecBinding
import com.autonomad.utils.Methods
import com.autonomad.utils.addOnItemTouchListener
import kotlinx.android.synthetic.main.fragment_category_list_spec.*

class CategoryList : Fragment() {
    private val viewModel by viewModels<CategoryListViewModel>()
    private val subCategoryAdapter = SubcategoryAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCategoryListSpecBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.getCategoryId(
            Methods.getToken(),
            arguments?.getString("idd").toString()
        )

        setAdapter()
        observer()
    }

    private fun observer() {
        viewModel.subcategory.observe(viewLifecycleOwner, Observer {
            subCategoryAdapter.updateRepoList(it)
        })
    }

    private fun setAdapter() {
        rv_subcategories.apply {
            setHasFixedSize(true)
            adapter = subCategoryAdapter
            addOnItemTouchListener { _, position ->
                findNavController().navigate(
                    R.id.action_claim_user_categoylist_to_detailCategPageOne, bundleOf(
                        "idd" to subCategoryAdapter.subCategories[position].id,
                        "sub_name" to subCategoryAdapter.subCategories[position].name
                    )
                )
            }
        }
    }
}
