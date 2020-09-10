package com.autonomad.ui.profile.driver_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.data.models.login.Message
import com.autonomad.data.models.penalty.Driver
import com.autonomad.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DriverPageViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var driver = MutableLiveData<List<Driver>>()
    var message = MutableLiveData<Message>()
    fun getDrivers(token: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getDriversTwo(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        Log.d("myLog1ViewModel", data.toString())
                        dataLoading.value = false
                        empty.value = false
                        driver.value = data.body()?.list
                        if (driver.value!!.isEmpty()) {
                            empty.value = true;
                        }
                    }
                    404 -> {
                        dataLoading.value = false
                    }
                    500 -> {
                        dataLoading.value = false
                    }
                    else -> {
                        dataLoading.value = false
                    }
                }


            }, {

            })

        compositeDisposable.add(disposable)
    }

    private val apiService = ApiGarageF.apiService
    private val mCheckAuto = MutableLiveData<Status<List<GarageCar>>>()
    val checkAutoSearch: LiveData<Status<List<GarageCar>>> = mCheckAuto
    fun search() {
        apiService.getGarageCars().subscribeOnIo().subscribe({
            mCheckAuto.value = it.listToStatus()
        }, {
            mCheckAuto.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }


    var favtwoo = MutableLiveData<Boolean>()
    fun deleteDriv(token: String, id: String, driverka: Driver) {
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .deleteDriverrr(token, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        Log.d("Tagii200", data.toString())
                        //message.value = data.body()
                        favtwoo.value = true

                        val temp = (driver.value as ArrayList<Driver>?)!!
                        temp.remove(driverka)
                        driver.value = temp
                        if (driver.value!!.isEmpty()) {
                            empty.value = true
                        }

                    }
                    404 -> {
                        Log.d("Tagii404", data.toString())
                        favtwoo.value = false

                    }
                    500 -> {
                        Log.d("Tagii500", data.toString())
                        favtwoo.value = false
                    }
                    else -> {
                        Log.d("Tagiielse", data.toString())
                        favtwoo.value = false
                    }
                }


            }, { throwable ->
                Log.d("Tag", throwable.toString())
            })
        compositeDisposable.add(disposable)
    }
}