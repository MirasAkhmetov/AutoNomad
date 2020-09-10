package com.autonomad.ui.claims.specialist_claims.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.NavigationFragment
import com.autonomad.utils.addOnItemTouchListener
import com.autonomad.utils.navigateBack
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_claims_specialist_home.*

class Home : NavigationFragment(R.layout.fragment_claims_specialist_home) {

    private val viewModel by viewModels<RequestsHomeViewModel>()

    private val categoriesAdapter = CategoriesAdapter()
    private val requestsAdapter = RequestsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setAdapter()
    }

    private fun initialize() {
        tv_show_all.setOnClickListener {
            val args = bundleOf("subcategoryId" to -1)
            findNavController().navigate(R.id.action_claim_specialist_home_to_repair_auto, args)
        }
        card_complete_profile.setOnClickListener {
            findNavController().navigate(R.id.action_claim_specialist_home_to_profile)
        }
        viewModel.specialist.observe(viewLifecycleOwner) {
            it.onSuccess {
                card_complete_profile.isVisible = isFullMaster == false
            }
            it.onFail { message ->
                tt("Не удалось загрузить анкету: $message")
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            if (!it.isLoading) {
                progress_bar.isVisible = false
                group_categories.isVisible = true
            }
            it.onSuccess { categoriesAdapter.items = list }
            it.onFail(::tt)
        }
        viewModel.requests.observe(viewLifecycleOwner) {
            if (!it.isLoading) {
                progress_bar.isVisible = false
                group_requests.isVisible = true
            }
            it.onSuccess {
                requestsAdapter.items = this
                requestsAdapter.finish()
            }
            it.onFail(::tt)
        }

        navigateBack(R.id.action_global_services)
    }

    private fun setAdapter() {
        rv_categories.apply {
            adapter = categoriesAdapter
            addOnItemTouchListener { _, position ->
                val args = bundleOf("categoryId" to categoriesAdapter.items[position].id)
                findNavController().navigate(R.id.action_claim_specialist_home_to_categoryList, args)
            }
        }

        rv_claims.apply {
            adapter = requestsAdapter
            addOnItemTouchListener { _, position ->
                val args = bundleOf("requestId" to requestsAdapter.items[position].id)
                findNavController().navigate(R.id.action_claim_specialist_home_to_detailClaim, args)
            }
        }
    }
}