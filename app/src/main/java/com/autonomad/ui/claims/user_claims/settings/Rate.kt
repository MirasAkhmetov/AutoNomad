package com.autonomad.ui.claims.user_claims.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autonomad.R
import com.autonomad.utils.BaseFragment

class Rate : BaseFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_claims_user_rate,container,false)
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
    }
}