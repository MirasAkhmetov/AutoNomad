package com.autonomad.ui.claims.user_claims.claims

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_claims_user_replies.*

class Replies : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_claims_user_replies, container, false)
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
        setAdapters()
    }

    private fun setAdapters() {
        val categories = ArrayList<String>()
        categories.add(("Ремонт авто"))
        categories.add(("Шиномонтаж"))
        categories.add(("Шиномонтаж"))
        categories.add(("Шиномонтаж"))
        categories.add(("Шиномонтаж"))
        categories.add(("Шиномонтаж"))
        categories.add(("Шиномонтаж"))

        replies_rv.layoutManager=LinearLayoutManager(context)
        replies_rv.adapter=RepliesAdapter(categories)
    }
}