package com.autonomad.ui.bottom_sheet.insurance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.utils.*

class InsuranceCarsViewModel : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mCars = MutableLiveData<Status<Page<GarageCar>>>()
    val cars: LiveData<Status<Page<GarageCar>>> by lazy {
        loadCars()
        mCars
    }

    fun loadCars() {
        Status.setLoaded(mCars)
        apiService.getGarageCars().subscribeAndDispose(mCars::fromResponse, mCars::fromThrowable)
    }
}