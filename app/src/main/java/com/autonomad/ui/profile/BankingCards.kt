package com.autonomad.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.banking_cards.BankingCardViewModel
import kotlinx.android.synthetic.main.fragment_profile_cards.*


class BankingCards : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_cards, container, false)
    }

    private val viewModel by viewModels<BankingCardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        add_banking_card.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_bankingCardAdd)
        }
    }
    private fun setAdapter() {
        val cards = ArrayList<String>()
        cards.add("****3432")
        cards.add("****3432")
//        val bankingCardAdapter = BankingCardsAdapter(cards,context as Context)
//            banking_card_rv.layoutManager = LinearLayoutManager(context)
//        banking_card_rv.setHasFixedSize(true)
//        banking_card_rv.adapter = bankingCardAdapter
    }
}