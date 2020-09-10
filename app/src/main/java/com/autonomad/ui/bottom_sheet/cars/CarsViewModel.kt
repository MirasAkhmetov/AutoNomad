package com.autonomad.ui.bottom_sheet.cars

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.main_page_car.Result
import com.autonomad.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CarsViewModel : BaseViewModel() {
    private var compositeDisposable = CompositeDisposable()
    private val apiService = ApiGarageFactory.instance.apiService

    var cars = MutableLiveData<List<Result>>()
    fun getCars() {
        dataLoading.value = true
        val disposable = apiService
            .getCars(Methods.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        cars.value = data.body()?.result
                        if (cars.value!!.isEmpty()) {
                            empty.value = true;
                        }
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                    500 -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                    else -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                }


            }, {
                println(it)
                dataLoading.value = false
                empty.value = false

            })

        compositeDisposable.add(disposable)
    }

    fun deleteCar(token: String, id: String, result: Result) {
        val disposable = apiService
            .deleteCar(token, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data)
                when (data.code()) {
                    200, 201, 204 -> {
                        dataLoading.value = false
                        empty.value = false
                        val temp = (cars.value as ArrayList<Result>?)!!
                        temp.remove(result)
                        cars.value = temp
                        if (cars.value!!.isEmpty()) {
                            empty.value = true;
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
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}