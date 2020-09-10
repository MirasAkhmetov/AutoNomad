package com.autonomad.ui.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.check_auto.CarLookup
import com.autonomad.data.models.check_auto.CarStateNumber
import com.autonomad.data.models.check_auto.CarVin
import com.autonomad.utils.*

class AddCarViewModel : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mCar = MutableLiveData<Status<CarLookup>>()
    val car: LiveData<Status<CarLookup>> = mCar

    private val mAdded = MutableLiveData<Status<Boolean>>()
    val added: LiveData<Status<Boolean>> = mAdded

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

    fun addCar(stateNumber: String? = null, vin: String) {
        val d = when {
            stateNumber != null -> apiService.setCar(CarStateNumber(stateNumber))
            vin.isNotEmpty() -> apiService.setCar(CarVin(vin))
            else -> return
        }
        mAdded.value = Status.loading
        d.subscribeAndDispose({
            mAdded.value = when {
                it.isSuccessful -> true.success
                it.errorBody()?.string()?.contains("exists") == true -> {
                    timber("contains")
                    "Машина с такими данными уже имеется в списке".fail
                }
                else -> it.message().fail
            }
        }, mAdded::fromThrowable)
    }

    fun clearCar() {
        mCar.value = Status.idle
    }

    companion object {
        const val REGNUM_NOT_FOUND = "regnum-not-found"
    }
}