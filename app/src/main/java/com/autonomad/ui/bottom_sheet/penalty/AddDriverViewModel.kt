package com.autonomad.ui.bottom_sheet.penalty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.penalty.DriverCheck
import com.autonomad.data.models.penalty.DriverCheckRequest
import com.autonomad.utils.*

class AddDriverViewModel : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mDriver = MutableLiveData<Status<DriverCheck>>()
    val driver: LiveData<Status<DriverCheck>> = mDriver

    fun penaltySearch(uin: String) {
        mDriver.value = Status.loading
        apiService.checkDriver(DriverCheckRequest(uin)).subscribeAndDispose({
            if (it.isSuccessful) mDriver.value = it.body().success
            else if (it.code() == 404 || it.code() == 500) mDriver.value = Status.idle
        }, {
            mDriver.value = it.localizedMessage.fail
        })
    }
}