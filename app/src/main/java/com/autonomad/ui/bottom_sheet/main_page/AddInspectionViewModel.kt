package com.autonomad.ui.bottom_sheet.main_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.check_auto.CarLookup
import com.autonomad.utils.*

class AddInspectionViewModel : DisposableViewModel() {
    val apiService = ApiGarageF.apiService

    private val mCar = MutableLiveData<Status<CarLookup>>()
    val car: LiveData<Status<CarLookup>> = mCar

    private var request = -1
    fun searchCar(stateNumber: String) {
        val currentRequest = ++request
        mCar.value = Status.loading
        apiService.getCarLookup(stateNumber).subscribeAndDispose({
            mCar.value = when {
                currentRequest != request -> Status.idle
                it.isSuccessful -> {
                    when (it.body()?.regnum) {
                        stateNumber -> it.body().success
                        REGNUM_NOT_FOUND -> REGNUM_NOT_FOUND.fail
                        else -> it.message().fail
                    }
                }
                else -> (it.message() ?: it.errorBody()?.string()).fail
            }
        }, {
            if (currentRequest == request) mCar.value = it.localizedMessage.fail
        })
    }

    companion object {
        const val REGNUM_NOT_FOUND = "regnum-not-found"
    }
}