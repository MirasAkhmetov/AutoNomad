package com.autonomad.ui.bottom_sheet.claim

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.Page
import com.autonomad.data.models.payment.*
import com.autonomad.utils.*
import org.json.JSONObject
import ru.cloudpayments.sdk.cp_card.CPCard

class PaymentViewModel(private val orderId: String) : DisposableViewModel() {
    private val apiService = ApiCapsSingle.apiService

    private val mCards = MutableLiveData<Status<Page<BankingCardResult>>>()
    val cards: LiveData<Status<Page<BankingCardResult>>> by lazy {
        loadBankingCards()
        mCards
    }

    private val mPaymentResult = MutableLiveData<Status<PaymentResult>>()
    val paymentResult: LiveData<Status<PaymentResult>> = mPaymentResult

    private val mFinished = MutableLiveData<Status<Boolean>>()
    val finished: LiveData<Status<Boolean>> = mFinished

    private fun loadBankingCards() {
        mCards.value = Status.loading
        apiService.getBankingCards().subscribeAndDispose(mCards::fromResponse, mCards::fromThrowable)
    }

    fun payWithNewCard(cardNumber: String, expireDate: String, cvv: String) {
        val cpCard = CPCard(cardNumber, expireDate, cvv)
        mPaymentResult.value = Status.loading
        apiService.getCredentials(orderId).subscribeOnIo().subscribe({
            val cpPublicId = it.body()?.cp_public_id
            if (cpPublicId == null) {
                mPaymentResult.value = "Не удалось получить данные для оплаты".fail
                return@subscribe
            }
            val checkout = Checkout(cpCard.cardCryptogram(cpPublicId), "0", "0")
            apiService.payWithNoSavedCard(orderId, checkout)
                .subscribeAndDispose({ paymentResponse ->
                    if (paymentResponse.isSuccessful) {
                        mPaymentResult.value = paymentResponse.body().success
                        return@subscribeAndDispose
                    }
                    val errorBody = paymentResponse.errorBody()?.string()
                    if (errorBody == null) {
                        mPaymentResult.value = paymentResponse.message().fail
                        return@subscribeAndDispose
                    } else {
                        try {
                            val message = JSONObject(errorBody).getString("message")
                            mPaymentResult.value = message.fail
                        } catch (e: Exception) {
                            mPaymentResult.value = paymentResponse.message().fail
                        }
                    }
                }, mPaymentResult::fromThrowable)
        }, mPaymentResult::fromThrowable).disposeOnCleared()
    }

    fun payWithSavedCard(cardId: String) {
        mPaymentResult.value = Status.loading
        apiService.payWithSavedCard(orderId, CardID(cardId))
            .subscribeAndDispose(mPaymentResult::fromResponse, mPaymentResult::fromThrowable)
    }

    fun threeDsFinish(md: String?, paRes: String?) {
        if (paRes == null || md == null) return
        val threeDSFinish = ThreeDSFinish(paRes, md)
        mFinished.value = Status.loading
        apiService.threeDSFinish(threeDSFinish).subscribeAndDispose({
            mFinished.value = if (it.isSuccessful) true.success else {
                val errorBody = it.errorBody()?.string()
                if (errorBody == null) it.message().fail
                else {
                    try {
                        val json = JSONObject(errorBody)
                        json.getString("message").fail
                    } catch (e: Exception) {
                        it.message().fail
                    }
                }
            }
        }, mFinished::fromThrowable)
    }
}

@Suppress("UNCHECKED_CAST")
class PaymentViewModelFactory(private val orderId: String) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = PaymentViewModel(orderId) as T
}