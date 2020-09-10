package com.autonomad.ui.profile.avto_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.utils.*

class CarsPageViewModel : BaseViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mCheckAuto = MutableLiveData<Status<List<GarageCar>>>()
    val checkAutoSearch: LiveData<Status<List<GarageCar>>> = mCheckAuto

    fun search() {
        mCheckAuto.value = Status.loading
        apiService.getGarageCars().subscribeOnIo().subscribe({
            mCheckAuto.value = it.listToStatus()
            if (it.isSuccessful && it.body()?.count == 0) empty.value = true
        }, {
            mCheckAuto.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }
}