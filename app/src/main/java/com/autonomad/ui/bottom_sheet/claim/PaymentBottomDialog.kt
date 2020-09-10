package com.autonomad.ui.bottom_sheet.claim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.MainActivity
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.check_auto.report.BankingCard
import com.autonomad.ui.check_auto.report.ThreeDS
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.bottom_sheet_claim_specialist_tariff_payment.*
import ru.cloudpayments.sdk.cp_card.CPCard
import ru.cloudpayments.sdk.three_ds.ThreeDsDialogFragment
import com.autonomad.ui.bottom_sheet.banking_cards.BankingCard as CardBottomDialog

class PaymentBottomDialog : RoundBottomDialog(), BankingCard, ThreeDS {

    private val orderId by lazy { arguments?.getString(ORDER_ID).orEmpty() }
    private val viewModel by viewModels<PaymentViewModel> { PaymentViewModelFactory(orderId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_claim_specialist_tariff_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (orderId.isEmpty()) {
            toast("Необходимо передать ID заказа")
            dismiss()
        }
        setTextListeners()
        MainActivity.setListener(this)
        val paymentSize = arguments?.getString(PAYMENT_SIZE)
        if (paymentSize != null) tv_payment_size.text = getString(R.string.price, paymentSize.substringBeforeLast("."))

        viewModel.cards.observe(viewLifecycleOwner) {
            it.onSuccess {
                if (count > 0) {
                    CardBottomDialog("check_auto", this@PaymentBottomDialog).show(
                        parentFragmentManager,
                        CardBottomDialog::class.java.simpleName
                    )
                }
            }
        }
        viewModel.finished.observe(viewLifecycleOwner) {
            checkButton()
            it.onSuccess { onComplete() }
            it.onFail(::tt)
        }
        viewModel.paymentResult.observe(viewLifecycleOwner) {
            progress_bar.isVisible = it.isLoading
            btn_pay.text = if (it.isLoading) "" else getString(R.string.pay)
            checkButton()
            it.onSuccess {
                if (status == "success")
                    onComplete()
                else if (status == "3d")
                    ThreeDsDialogFragment.newInstance(message.acs_url, message.transaction_id, message.pa_req)
                        .show(childFragmentManager, "3ds")
            }
            it.onFail(::tt)
        }

        btn_pay.setOnClickListener {
            if (validate())
                viewModel.payWithNewCard(getCardNumber(), getExpireDate(), et_cvv.text.toString())
            else toast("Проверьте корректность введённых данных и повторите попытку")
        }
    }

    private fun setTextListeners() {
        et_card_number.doAfterTextChanged {
            if (it == null) return@doAfterTextChanged
            val trimmed = it.trim().toString()
            if (et_card_number.text.toString() != trimmed) {
                et_card_number.setText(trimmed)
                et_card_number.setSelection(et_card_number.text.length)
                return@doAfterTextChanged
            }
            if (it.length in setOf(5, 10, 15)) {
                et_card_number.setText(StringBuilder(it).insert(it.length - 1, " "))
                et_card_number.setSelection(et_card_number.text.length)
            }
            if (it.length == 19) {
                et_date.requestFocus()
                checkButton()
            }
        }
        et_date.doAfterTextChanged {
            if (it == null || it.endsWith("/")) return@doAfterTextChanged
            if (it.length == 3) {
                et_date.setText(StringBuffer(it).insert(it.length - 1, "/").toString())
                et_date.setSelection(et_date.text.length)
            }
            if (it.length == 5) {
                et_cvv.requestFocus()
                checkButton()
            }
        }
        et_cvv.doAfterTextChanged { if (it?.length == 3) checkButton() }
    }

    private fun validate() = CPCard.isValidNumber(getCardNumber()) && CPCard.isValidExpDate(getExpireDate())

    override fun callFunction(string: String) {
        viewModel.payWithSavedCard(string)
    }

    private fun checkButton() {
        btn_pay.isEnabled = getCardNumber().length == 16 && getExpireDate().length == 4 && et_cvv.text.length == 3 &&
                viewModel.paymentResult.value?.isLoading != true && viewModel.finished.value?.isLoading != true
    }

    private fun getCardNumber() = et_card_number.text.toString().replace(" ", "")

    private fun getExpireDate() = et_date.text.toString().replace("/", "")

    private fun onComplete() {
        dismiss()
        findNavController().popBackStack()
    }

    companion object {
        private const val ORDER_ID = "orderId"
        private const val PAYMENT_SIZE = "paymentSize"

        fun show(fragmentManager: FragmentManager, orderId: String, paymentSize: String?) {
            PaymentBottomDialog().apply {
                arguments = bundleOf(ORDER_ID to orderId, PAYMENT_SIZE to paymentSize)
                show(fragmentManager, javaClass.simpleName)
            }
        }
    }

    override fun error() {
        toast("Произошла ошибка при оплате")
    }

    override fun success(md: String?, paRes: String?) {
        viewModel.threeDsFinish(md, paRes)
    }
}