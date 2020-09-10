package com.autonomad.ui.penalty.penalty_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.addPage
import com.autonomad.data.models.penalty.PaymentHistory
import com.autonomad.utils.*

class PenaltyHistoryViewModel : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mHistory = MutableLiveData<Status<Page<PaymentHistory>>>()
    val history: LiveData<Status<Page<PaymentHistory>>> by lazy {
        loadHistory()
        mHistory
    }

    private var page = 0

    private fun loadHistory() {
        mHistory.value = Status.loading
        apiService.getPenaltyHistory().subscribeAndDispose({
            if (mHistory.addPage(page, it)) page++
        }, {
            mHistory.value = it.localizedMessage.fail
        })
    }
}
