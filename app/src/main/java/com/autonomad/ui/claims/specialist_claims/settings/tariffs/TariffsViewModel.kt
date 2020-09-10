package com.autonomad.ui.claims.specialist_claims.settings.tariffs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.claims.Tariff
import com.autonomad.data.models.claims.TariffOrder
import com.autonomad.data.models.claims.TariffPost
import com.autonomad.utils.*

class TariffsViewModel : DisposableViewModel() {
    private val apiService = ApiRequestsFactory.apiService

    private val mTariffs = MutableLiveData<Status<Page<Tariff>>>()
    val tariffs: LiveData<Status<Page<Tariff>>> by lazy {
        loadTariffs()
        mTariffs
    }

    private val mOrder = MutableLiveData<Status<TariffOrder>>()
    val order: LiveData<Status<TariffOrder>> = mOrder

    private fun loadTariffs() {
        mTariffs.value = Status.loading
        apiService.getTariffs().subscribeAndDispose({
            mTariffs.value = it.toStatus()
        }, mTariffs::fromThrowable)
    }

    fun orderTariff(id: Int) {
        mOrder.value = Status.loading
        apiService.orderTariff(TariffPost(id)).subscribeAndDispose({
            val orderId = it.body()?.id
            if (orderId != null) getOrder(orderId) else mOrder.value = it.message().fail
        }, mOrder::fromThrowable)
    }

    private fun getOrder(id: Int) {
        apiService.getTariffOrder(id).subscribeAndDispose(mOrder::fromResponse, mOrder::fromThrowable)
    }
}