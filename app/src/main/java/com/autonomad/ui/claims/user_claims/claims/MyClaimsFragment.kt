package com.autonomad.ui.claims.user_claims.claims

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.databinding.FragmentClaimsUserMyClaimsBinding
import com.autonomad.utils.Methods
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_claims_user_my_claims.*

class MyClaimsFragment : Fragment() {
    private val viewModel by viewModels<ClaimsViewModel>()
    private lateinit var claims_adapter: ClaimsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentClaimsUserMyClaimsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateBack(R.id.action_global_services)
        createClaim.setOnClickListener {
            findNavController().navigate(R.id.action_Claim_to_oneclaim)
        }
        btn_open_claim.setOnClickListener {
            findNavController().navigate(R.id.action_Claim_to_oneclaim)
        }
        setAdapter()

        viewModel.getServicereq(Methods.getToken())
        viewModel.services.observe(viewLifecycleOwner, Observer {
            claims_adapter.services = it
        })
    }

    private fun setAdapter() {
        claims_rv.layoutManager = LinearLayoutManager(context)
        claims_rv.setHasFixedSize(true)
        claims_adapter = ClaimsAdapter(viewModel, context as Context)
        claims_rv.adapter = claims_adapter
        //claims_rv.adapter = claims_adapter
    }
}