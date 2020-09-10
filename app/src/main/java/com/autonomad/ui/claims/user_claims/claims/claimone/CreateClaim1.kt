package com.autonomad.ui.claims.user_claims.claims.claimone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.databinding.FragmentCreateOneclaimBinding
import com.autonomad.ui.claims.user_claims.home.ClaimHomeViewModel
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.fragment_create_oneclaim.*
import kotlinx.android.synthetic.main.fragment_create_oneclaim.ic_back

class CreateClaim1 : BaseFragment() {
    private lateinit var ViewModel: FragmentCreateOneclaimBinding
    private var selectedPosition: Int = -1
    private var mystr: String = ""
    private lateinit var claim_adapter: CreateClaimOneAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentCreateOneclaimBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@CreateClaim1)
                .get(ClaimHomeViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.viewmodel?.getCategories(Methods.getToken())
    }

    override fun initialise() {

    }

    override fun setObservers() {
        ViewModel.viewmodel?.categories?.observe(viewLifecycleOwner, Observer {
            claim_adapter.updateRepoList(it)
        })
    }

    override fun setAdapter() {

        val viewModel1 = ViewModel.viewmodel
        if (viewModel1 != null) {
            uslug_type_rv.layoutManager = LinearLayoutManager(context)
            uslug_type_rv.setHasFixedSize(true)
            claim_adapter = CreateClaimOneAdapter(ViewModel.viewmodel!!, context as Context)
            uslug_type_rv.adapter = claim_adapter

        }
    }

    override fun setOnClickListener() {
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }


}
