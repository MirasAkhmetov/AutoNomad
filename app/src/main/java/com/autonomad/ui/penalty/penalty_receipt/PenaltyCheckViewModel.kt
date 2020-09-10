package com.autonomad.ui.penalty.penalty_receipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.penalty.PaymentHistory
import com.autonomad.utils.*

class PenaltyCheckViewModel : DisposableViewModel() {
    private val apiGarage = ApiGarageF.apiService

    private val mResult = MutableLiveData<Status<PaymentHistory>>()
    val result: LiveData<Status<PaymentHistory>> = mResult

    fun penaltyHistorySearch(id: Int) {
        apiGarage.getPenaltyHistory(id).subscribeAndDispose({
            mResult.value = it.toStatus()
        }, {
            mResult.value = it.localizedMessage.fail
            timber(it.localizedMessage)
        })
    }
}