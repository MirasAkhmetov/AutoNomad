package com.autonomad.ui.claims.specialist_claims.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.addPage
import com.autonomad.data.models.claims.ResponseUpdate
import com.autonomad.data.models.claims.ServiceResponse
import com.autonomad.utils.*

class FeedbackViewModel : DisposableViewModel() {

    private val apiService = ApiRequestsFactory.apiService

    private val mRequests = MutableLiveData<Status<Page<ServiceResponse>>>()
    val requests: LiveData<Status<Page<ServiceResponse>>> = mRequests

    private val mActive = MutableLiveData<Status<Page<ServiceResponse>>>()
    val active: LiveData<Status<Page<ServiceResponse>>> = mActive

    private val mFinished = MutableLiveData<Status<Page<ServiceResponse>>>()
    val finished: LiveData<Status<Page<ServiceResponse>>> = mFinished

    private var requestsPage = 0
    private var activePage = 0
    private var finishedPage = 0

    private fun loadRequests() {
        mRequests.value = Status.loading
        apiService.getServiceResponses(1, 20, 20 * requestsPage).subscribeOnIo().subscribe({
            if (mRequests.addPage(requestsPage, it)) requestsPage++
        }, {
            Status.setFailed(mRequests, it.localizedMessage)
        }).disposeOnCleared()
    }

    private fun loadActive() {
        mActive.value = Status.loading
        apiService.getServiceResponses(5, 20, 20 * activePage).subscribeOnIo().subscribe({
            if (mActive.addPage(activePage, it)) activePage++
        }, {
            Status.setFailed(mActive, it.localizedMessage)
        }).disposeOnCleared()
    }

    private fun loadFinished() {
        mFinished.value = Status.loading
        apiService.getServiceResponses(2, 20, 20 * finishedPage).subscribeOnIo().subscribe({
            if (mFinished.addPage(finishedPage, it)) finishedPage++
        }, {
            Status.setFailed(mFinished, it.localizedMessage)
        }).disposeOnCleared()
    }

    fun updateResponse(responseId: Int, status: Int, position: Int) {
        val (load, liveData) = when (position) {
            0 -> ::loadRequests to mRequests
            1 -> ::loadActive to mActive
            else -> ::loadFinished to mFinished
        }
        val body = ResponseUpdate(status)
        apiService.updateServiceResponse(responseId, body).subscribeOnIo().subscribe({
            if (it.isSuccessful) load()
            else liveData.value = it.message().fail
        }, {
            liveData.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }

    fun loadMore(position: Int) {
        when (position) {
            0 -> loadRequests()
            1 -> loadActive()
            2 -> loadFinished()
        }
    }
}