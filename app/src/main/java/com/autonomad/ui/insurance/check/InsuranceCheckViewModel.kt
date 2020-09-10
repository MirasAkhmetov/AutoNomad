package com.autonomad.ui.insurance.check

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.insurance.InsuranceCheck
import com.autonomad.data.models.insurance.NewCheck
import com.autonomad.utils.*

class InsuranceCheckViewModel : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mResult = MutableLiveData<Status<InsuranceCheck>>()
    val result: LiveData<Status<InsuranceCheck>> = mResult

    fun setData(check: NewCheck) {
        apiService.checkInsurance(check).subscribeAndDispose(mResult::fromResponse, mResult::fromThrowable)
    }
}