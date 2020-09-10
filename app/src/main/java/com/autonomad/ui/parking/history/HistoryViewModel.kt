package com.autonomad.ui.parking.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.parking.ParkingOrder
import com.autonomad.utils.ApiGarageFactory
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.Methods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HistoryViewModel :BaseViewModel(){
    var data = MutableLiveData<List<ParkingOrder>>()
    lateinit var compositeDisposable: CompositeDisposable
    fun getHistory(periodType: Int? = null) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getParkingOrders(Methods.getToken(), periodType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        this.data.value = data.body()?.results!!
                        if (this.data.value.isNullOrEmpty()) {
                            empty.value = true
                        }

                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }

            }, { throwable ->
                Log.d("Tag", throwable.toString())
                empty.value = true
                dataLoading.value = false
            })
        compositeDisposable.add(disposable)
    }


}