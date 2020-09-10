package com.autonomad.ui.penalty.penalties_of_person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.penalty.MakeOrder
import com.autonomad.data.models.penalty.PenaltyDetail
import com.autonomad.data.models.penalty.PenaltyId
import com.autonomad.utils.*

class PenaltyDetailViewModel(private val id: Int) : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mUrl = MutableLiveData<Status<MakeOrder>>()
    val url: LiveData<Status<MakeOrder>> = mUrl

    private val mResult = MutableLiveData<Status<PenaltyDetail>>()
    val result: LiveData<Status<PenaltyDetail>> by lazy {
        penaltySearch()
        mResult
    }

    private fun penaltySearch() {
        mResult.value = Status.loading
        apiService.penaltyDetail(id).subscribeOnIo().subscribe({
            mResult.value = it.toStatus()
        }, {
            mResult.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }

    fun makeOrder() {
        mUrl.value = Status.loading
        apiService.makeOrder(PenaltyId(id)).subscribeOnIo().subscribe({
            mUrl.value = it.toStatus()
            mUrl.value = Status.idle
        }, {
            mUrl.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }
}

@Suppress("UNCHECKED_CAST")
class PenaltyDetailFactory(private val id: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = PenaltyDetailViewModel(id) as T
}