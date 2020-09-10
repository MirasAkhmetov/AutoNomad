package com.autonomad.ui.bottom_sheet.banking_cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.databinding.BottomSheetBankingCardsBinding
import com.autonomad.ui.check_auto.report.BankingCard
import com.autonomad.ui.parking.home.Home
import com.autonomad.utils.RoundedBottomSheetDialogFragment
import com.autonomad.utils.addOnItemTouchListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_banking_cards.*

class BankingCard(private val state: String, private val listener: BankingCard) : RoundedBottomSheetDialogFragment() {
    private val viewModel by viewModels<BankingCardViewModel>()
    private val cardAdapter = BankingCardAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetBankingCardsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as? BottomSheetDialog
            val bottomSheetInternal = d?.findViewById<View>(R.id.design_bottom_sheet) ?: return@setOnShowListener
            BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        return binding.root
    }

    override fun initialise() {
        when (state) {
            "check_auto" -> add_banking_card.visibility = View.GONE
            else -> add_banking_card.visibility = View.VISIBLE
        }
//        add_banking_card.setOnClickListener {
//            dismiss()
//            val banking_card =
//                BankingCardAdd(
//                    context
//                )
//            banking_card.window?.findViewById<View>(R.id.design_bottom_sheet)
//                ?.setBackgroundResource(android.R.color.transparent)
//            banking_card.show()
//        }
    }

    override fun setObservers() {
        viewModel.getBankingCards()
        viewModel.bankingCards.observe(viewLifecycleOwner) {
            cardAdapter.cars = it
        }
    }

    override fun setAdapter() {
        banking_card_rv.adapter = cardAdapter
        banking_card_rv.addOnItemTouchListener { _, position ->
            when (state) {
                "check_auto" -> {
                    listener.callFunction(cardAdapter.cars[position].id)
                    dismiss()
                }
                "parking" -> {
                    listener.callFunction(cardAdapter.cars[position].id + " " + cardAdapter.cars[position].card_last_four)
                    dismiss()
                }
            }
        }
    }

    override fun setOnClickListener() {
        add_banking_card.setOnClickListener {
            Home.showBottomSheetAddBankingCard()
            dismiss()
        }
    }
}