package com.autonomad.ui.check_auto.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.payment.CardID
import com.autonomad.data.models.payment.Checkout
import com.autonomad.data.models.payment.ThreeDSFinish
import com.autonomad.databinding.FragmentCheckAutoPaymentBinding
import com.autonomad.utils.BaseFragment
import com.autonomad.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_check_auto_payment.*
import ru.cloudpayments.sdk.cp_card.CPCard
import ru.cloudpayments.sdk.three_ds.ThreeDsDialogFragment

class Payment : BaseFragment(), ThreeDS, BankingCard {
    private lateinit var ViewModel: FragmentCheckAutoPaymentBinding
    private lateinit var order_id: String
    private lateinit var reportPrice: String
    private var ticket_id = 0
    private lateinit var cpCard: CPCard
    private lateinit var cardCryptogram: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel =
            FragmentCheckAutoPaymentBinding.inflate(inflater, container, false)
                .apply {
                    viewmodel = ViewModelProvider(this@Payment)
                        .get(PaymentViewModel::class.java)
                    lifecycleOwner = viewLifecycleOwner
                }
        return ViewModel.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ticket_id = arguments?.getInt("id") ?: 0
        order_id = arguments?.getString("order_id").toString()
        reportPrice = arguments?.getString("price").toString()
        ViewModel.viewmodel?.getBankingCards()
        MainActivity.setListener(this)
        super.onViewCreated(view, savedInstanceState)
    }


    override fun initialise() {
        price.text = reportPrice
        card_number.doOnTextChanged { text, start, count, after ->
            val text = text.toString()
            if (text.endsWith(" "))
                return@doOnTextChanged
            when (card_number.text.length) {
                5 -> {
                    card_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
                10 -> {
                    card_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
                15 -> {
                    card_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
                20 -> {
                    card_number.setText(
                        StringBuilder(text).deleteCharAt(
                            text.length - 1
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
            }
        }
        expire_date_number.doOnTextChanged { text, start, count, after ->
            val text = text.toString()
            if (text.endsWith("/"))
                return@doOnTextChanged
            when (expire_date_number.text.length) {
                3 -> {
                    expire_date_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            "/"
                        ).toString()
                    )
                    expire_date_number.setSelection(expire_date_number.text.length)
                }
                6 -> {
                    expire_date_number.setText(
                        StringBuilder(text).deleteCharAt(
                            text.length - 1
                        ).toString()
                    )
                    expire_date_number.setSelection(expire_date_number.text.length)
                }
            }
        }
        cvv_number.doOnTextChanged { text, start, count, after ->
            val text = text.toString()
            when (cvv_number.text.length) {
                4 -> {
                    cvv_number.setText(
                        StringBuilder(text).deleteCharAt(
                            text.length - 1
                        ).toString()
                    )
                    cvv_number.setSelection(cvv_number.text.length)
                }
            }
        }
    }

    override fun setObservers() {
        ViewModel.viewmodel?.credential?.observe(viewLifecycleOwner, Observer {
            when {
                it.cp_public_id.length > 1 -> {
                    cpCard = CPCard(
                        card_number.text.toString().replace(" ", ""),
                        expire_date_number.text.toString().replace("/", ""),
                        cvv_number.text.toString()
                    )
                    cardCryptogram = cpCard.cardCryptogram(it.cp_public_id)
                    ViewModel.viewmodel?.payWithNoSavedCars(
                        requireView(),
                        order_id,
                        Checkout(cardCryptogram, "Nursat", "5")
                    )
                }
            }
        })
        ViewModel.viewmodel?.paymentResult?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                "success" -> {
                    val bundle = bundleOf("id" to ticket_id)
                    findNavController().navigate(R.id.action_payment_to_detailTicket, bundle)
                }
                "3d" -> {
                    ThreeDsDialogFragment.newInstance(
                        it.message.acs_url,
                        it.message.transaction_id,
                        it.message.pa_req
                    ).show(childFragmentManager, "3DS")
                }
            }
        })
        ViewModel.viewmodel?.threeDS?.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    val bundle = bundleOf("id" to ticket_id)
                    findNavController().navigate(R.id.action_payment_to_detailTicket, bundle)
                }
                false -> {
                    Snackbar.make(requireView(), "Произошла ошибка", Snackbar.LENGTH_LONG).show()
                }
            }
        })
        ViewModel.viewmodel?.bankingCards?.observe(viewLifecycleOwner, Observer {
            when {
                it.count.toInt() > 0 -> {
                    val bottomsheet =
                        com.autonomad.ui.bottom_sheet.banking_cards.BankingCard("check_auto", this)
                    bottomsheet.show(parentFragmentManager, "")
                }
            }
        })

    }

    override fun setAdapter() {

    }

    override fun setOnClickListener() {
        pay.setOnClickListener {
            if (validate()) {
                ViewModel.viewmodel?.getCredential(order_id)
            } else {
                Snackbar.make(requireView(), "Not validated", Snackbar.LENGTH_LONG).show()
            }
        }
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun validate(): Boolean {
        return CPCard.isValidNumber(
            card_number.text.toString().replace(" ", "")
        ) && CPCard.isValidExpDate(
            expire_date_number.text.toString().replace("/", "")
        ) && cvv_number.text.length==3
    }

    override fun success(md: String?, paRes: String?) {
        ViewModel.viewmodel?.threeDSFinish(requireView(), ThreeDSFinish(paRes!!, md!!))
    }

    override fun error() {
        Snackbar.make(requireView(), "Произошла ошибка", Snackbar.LENGTH_LONG).show()
    }

    override fun callFunction(card_id: String) {
        ViewModel.viewmodel?.payWithSavedCards(requireView(), order_id, CardID(card_id))
    }
}