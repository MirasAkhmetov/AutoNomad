package com.autonomad.ui.profile.avto_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.main_page_car.*
import com.autonomad.network.ApiGarage
import com.autonomad.utils.*

class CarsPageDetailViewModel(private val carId: Int, private val apiService: ApiGarage) : BaseViewModel() {

    private val mDrivers = MutableLiveData<Status<List<GarageDriver>>>()
    val drivers: LiveData<Status<List<GarageDriver>>> = mDrivers

    private val mCar = MutableLiveData<Status<Result>>()
    val car: LiveData<Status<Result>> by lazy {
        getCarById()
        mCar
    }

    private fun getCarById() {
        mCar.value = Status.loading
        apiService.getCarByID(carId).subscribeAndDispose({
            if (it.isSuccessful) {
                mCar.value = it.body().success
                mDrivers.value = it.body()?.drivers.success
            } else {
                mCar.value = it.convert()
            }
        }, mCar::fromThrowable)
    }

    private val mUpdate = MutableLiveData<Status<Boolean>>()
    val update: LiveData<Status<Boolean>> = mUpdate
    fun patchIsMain(isMain: Boolean) {
        mUpdate.value = Status.loading
        apiService.patchIsMain(carId, IsMainUpdate(isMain)).subscribeAndDispose({
            if (it.isSuccessful) mUpdate.value = isMain.success
            else {
                mUpdate.value = (!isMain).success
                mUpdate.value = when (it.code()) {
                    406 -> "Вы не можете выбрать больше одного основного авто".fail
                    else -> it.message().fail
                }
            }
        }, mUpdate::fromThrowable)
    }

    private val mDelete = MutableLiveData<Status<Boolean>>()
    val delete: LiveData<Status<Boolean>> = mDelete
    fun deleteCar() {
        mDelete.value = Status.loading
        apiService.deleteCar(carId).subscribeAndDispose({
            mDelete.value = if (it.isSuccessful) true.success else it.message().fail
        }, mDelete::fromThrowable)
    }

    fun updateCarSrts(carId: Int, srts: String): LiveData<Status<Int>> {
        val srtsUpdate = MutableLiveData<Status<Int>>()
        apiService.updateSrts(carId, SrtsUpdate(srts)).subscribeAndDispose({
            srtsUpdate.value = if (it.isSuccessful) carId.success else it.message().fail
        }, srtsUpdate::fromThrowable)
        return srtsUpdate
    }

    private val titleUpdate = MutableLiveData<Status<Boolean>>()
    fun setCarTitle(title: String): LiveData<Status<Boolean>> {
        apiService.updateCarTitle(carId, CarTitleUpdate(title)).subscribeAndDispose({
            titleUpdate.value = if (it.isSuccessful) {
                getCarById()
                true.success
            } else it.message().fail
        }, titleUpdate::fromThrowable)
        return titleUpdate
    }
}