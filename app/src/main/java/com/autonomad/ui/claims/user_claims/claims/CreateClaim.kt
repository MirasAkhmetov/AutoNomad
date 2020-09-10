package com.autonomad.ui.claims.user_claims.claims

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autonomad.R
import com.autonomad.utils.BaseFragment

class CreateClaim : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_claims_user_create_claim, container, false)
    }

    override fun initialise() {

    }

    override fun setObservers() {

    }

    override fun setAdapter() {

    }

    override fun setOnClickListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val services = ArrayList<String>()
        services.add(("Ремонт авто"))
        services.add(("Шиномонтаж"))

//        images_rv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//        val images_adapter= ImageAdapter(services)
//        images_rv.adapter=images_adapter

    }
}