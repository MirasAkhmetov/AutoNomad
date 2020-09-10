package com.autonomad.ui.profile.settings.BankCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.banking_cards.BankingCardViewModel
import com.autonomad.ui.profile.BankingCardsAdapter
import kotlinx.android.synthetic.main.fragment_profile_bank.*

class BankSpisok : Fragment() {
    private val viewModel by viewModels<BankingCardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }

        addCard.setOnClickListener {
            findNavController().navigate(R.id.action_bankSpisok_to_bankingCardAdd)
        }

        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_bar.visibility = VISIBLE
                main_content.visibility = GONE
            } else {
                progress_bar.visibility = GONE
                main_content.visibility = VISIBLE
            }
        })
        viewModel.bankingCards.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                one.visibility = VISIBLE
                two.visibility = VISIBLE
                banking_card_rv.visibility = GONE
            } else {
                one.visibility = GONE
                two.visibility = GONE
                banking_card_rv.visibility = VISIBLE

                val adapter = BankingCardsAdapter(it, requireContext())
                banking_card_rv.adapter = adapter
            }
        })

        viewModel.getBankingCards()

    }
}