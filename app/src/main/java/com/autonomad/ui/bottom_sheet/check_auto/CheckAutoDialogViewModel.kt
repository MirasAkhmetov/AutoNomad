package com.autonomad.ui.bottom_sheet.check_auto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.check_auto.CarLookup
import com.autonomad.utils.*

class CheckAutoDialogViewModel : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mCar = MutableLiveData<Status<CarLookup>>()
    val car: LiveData<Status<CarLookup>> = mCar

    private var request = -1
    fun searchCar(stateNumber: String) {
        val currentRequest = ++request
        mCar.value = Status.loading
        apiService.getCarLookup(stateNumber).subscribeAndDispose({
            mCar.value = when {
                currentRequest != request -> Status.idle
                it.isSuccessful -> it.body().success
                else -> (it.message() ?: it.errorBody()?.string()).fail
            }
        }, {
            if (currentRequest == request) mCar.value = it.localizedMessage.fail
        })
    }

    fun clearCar() {
        mCar.value = Status.idle
    }
}