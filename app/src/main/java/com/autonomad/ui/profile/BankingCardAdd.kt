package com.autonomad.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_profile_set_banking_card.*

class BankingCardAdd : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_set_banking_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener { activity?.onBackPressed() }
        add_banking_card.setOnClickListener { activity?.onBackPressed() }
       // navigateBack(R.id.action_global_profile)
    }
}