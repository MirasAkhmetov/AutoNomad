package com.autonomad.ui.claims.specialist_claims.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.claims.Category
import com.autonomad.data.models.claims.ServiceRequest
import com.autonomad.data.models.claims.Specialist
import com.autonomad.utils.*

class RequestsHomeViewModel : DisposableViewModel() {
    private val apiService = ApiRequestsFactory.apiService

    private val mCategories = MutableLiveData<Status<Page<Category>>>(Status.loading)
    val categories: LiveData<Status<Page<Category>>> by lazy {
        getCategories()
        mCategories
    }

    private val mSpecialist = MutableLiveData<Status<Specialist>>()
    val specialist: LiveData<Status<Specialist>> by lazy {
        getSpecialistProfile()
        mSpecialist
    }

    private val mRequests = MutableLiveData<Status<List<ServiceRequest>>>(Status.loading)
    val requests: LiveData<Status<List<ServiceRequest>>> by lazy {
        getRequests()
        mRequests
    }

    private fun getSpecialistProfile() {
        apiService.logSpecialist().subscribeAndDispose(mSpecialist::fromResponse, mSpecialist::fromThrowable)
    }

    private fun getCategories() {
        mCategories.value = Status.loading
        apiService.getCategories().subscribeAndDispose(mCategories::fromResponse, mCategories::fromThrowable)
    }

    private fun getRequests() {
        mRequests.value = Status.loading
        apiService.getServiceRequests(3, 0).subscribeAndDispose({
            mRequests.value = it.listToStatus()
        }, mRequests::fromThrowable)
    }
}