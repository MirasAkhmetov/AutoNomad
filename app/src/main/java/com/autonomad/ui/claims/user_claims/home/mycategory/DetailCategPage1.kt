package com.autonomad.ui.claims.user_claims.home.mycategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.data.models.claims.MasterFilter
import com.autonomad.databinding.FragmentClaimsDetailCategPageBinding
import com.autonomad.ui.claims.user_claims.home.mymasters.DetailSubModelView
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.fragment_claims_detail_categ_page.*

class DetailCategPage1 : BaseFragment() {
    private val viewModel: DetailSubModelView by viewModels()
    private val mastersAdapter by lazy {
        DetailSpisokAdapter(viewModel, requireContext())
    }
    private val subCategoryId by lazy { arguments?.getInt("idd") ?: -1 }
    private var selectedSortType: MasterFilter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentClaimsDetailCategPageBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun initialise() {
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMasters(Methods.getToken(), subCategoryId)

        text_filter.text = arguments?.getString("sub_name").toString()

        ic_sort.setOnClickListener {
            FilterDialog(selectedSortType?.id, ::onFilterApplied).show(childFragmentManager, "ClaimFilter")
        }
    }

    override fun setObservers() {
        viewModel.subMaster.observe(viewLifecycleOwner, Observer {
            tvMasterCount.text = it.count.toString().plus(" специалистов")
        })
        viewModel.subMasterList.observe(viewLifecycleOwner, Observer {
            mastersAdapter.updateRepoList(it)
        })
        viewModel.favBoldima.observe(viewLifecycleOwner, Observer {})
    }

    override fun setAdapter() {
        feedback_rv.layoutManager = LinearLayoutManager(context)
        feedback_rv.adapter = mastersAdapter
    }

    override fun setOnClickListener() {

    }

    private fun onFilterApplied(filter: MasterFilter?) {
        selectedSortType = filter
        filter?.let {
            when (it.type) {
                MasterFilter.Type.RATING -> {
                    viewModel.getMasters(Methods.getToken(), subCategoryId, sortByRating = it.sortOrder)
                }
                MasterFilter.Type.POPULARITY -> {
                    viewModel.getMasters(Methods.getToken(), subCategoryId, sortByPopularity = it.sortOrder)
                }
            }
        } ?: viewModel.getMasters(Methods.getToken(), subCategoryId)
    }
}