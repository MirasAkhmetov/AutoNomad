package com.autonomad.ui.claims.specialist_claims.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.ServiceResponseCreate
import com.autonomad.data.models.claims.ServiceRequest
import com.autonomad.data.models.claims.ServiceResponse
import com.autonomad.utils.*

class DetailViewModel(private val id: Int, private val isResponse: Boolean) : DisposableViewModel() {

    private val apiService = ApiRequestsFactory.apiService

    private val mRequest = MutableLiveData<Status<ServiceRequest>>()
    val request: LiveData<Status<ServiceRequest>> = mRequest

    private val mResponded = MutableLiveData<Status<ServiceResponseCreate>>()
    val responded: LiveData<Status<ServiceResponseCreate>> = mResponded

    private val mResponse = MutableLiveData<Status<ServiceResponse>>()
    val response: LiveData<Status<ServiceResponse>> = mResponse

    fun loadData() {
        if (isResponse) {
            mResponse.value = Status.loading
            apiService.getServiceResponse(id).subscribeAndDispose(mResponse::fromResponse, mResponse::fromThrowable)
        } else {
            mRequest.value = Status.loading
            if (Methods.isSpecialist()) {
                apiService.getServiceRequestById(id)
                    .subscribeAndDispose(mRequest::fromResponse, mRequest::fromThrowable)
            } else {
                apiService.getMyServiceRequestById(id)
                    .subscribeAndDispose(mRequest::fromResponse, mRequest::fromThrowable)
            }
        }
    }

    fun respond() {
        if (isResponse) return
        mResponded.value = Status.loading
        apiService.makeResponse(id, ServiceResponseCreate(id))
            .subscribeAndDispose(mResponded::fromResponse, mResponded::fromThrowable)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val response: Int, private val isResponse: Boolean = false) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = DetailViewModel(response, isResponse) as T
}