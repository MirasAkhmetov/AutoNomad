package com.autonomad.ui.parking.history

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.parking.ParkingOrder
import com.autonomad.utils.ApiGarageFactory
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.Methods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailReceiptViewModel :BaseViewModel(){
    lateinit var compositeDisposable: CompositeDisposable
    val result=MutableLiveData<ParkingOrder>()
    fun getData(id: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .checkParkingOrder(Methods.getToken(), id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        val detailTicket = data.body()
                        detailTicket?.date1 =
                            detailTicket?.updatedAt?.substringBefore("T").toString()
                                .replace("-", ".")
                        detailTicket?.date2 =
                            detailTicket?.updatedAt?.substringAfter("T")?.substringBefore(".")
                                .toString()
                        result.value = detailTicket
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

}