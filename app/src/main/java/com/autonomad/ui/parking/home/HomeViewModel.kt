package com.autonomad.ui.parking.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.parking.*
import com.autonomad.data.models.payment.*
import com.autonomad.utils.ApiCapsFactory
import com.autonomad.utils.ApiGarageFactory
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.Methods
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : BaseViewModel() {
    val pageInfo = MutableLiveData<pageInfo>()
    var parkingZones = MutableLiveData<List<ParkingZone>>()
    var registrated = MutableLiveData<Boolean>()
    var credential = MutableLiveData<Credentials>()
    var paymentResult = MutableLiveData<PaymentResult>()
    var order = MutableLiveData<ParkingOrder>()
    var threeDS = MutableLiveData<Boolean>()
    var searchResultParkingZones = ArrayList<ParkingZone>()

    lateinit var compositeDisposable: CompositeDisposable


    fun initialise() {
        pageInfo.value = pageInfo(
            "0",
            "0",
            "15",
            "",
            "",
            ""
        )
    }

    fun setData(pageInfo: pageInfo) {
        this.pageInfo.value = pageInfo
    }

    fun checkPrice(checkPrice: CheckPrice) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .checkPrice(Methods.getToken(), checkPrice)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                dataLoading.value = false
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        empty.value = false
                        val temp = pageInfo.value
                        if (temp?.price != data.body()?.response?.totalCharge.toString()) {
                            temp?.price = data.body()?.response?.totalCharge.toString()
                            pageInfo.value = temp
                        }
                    }
                    404 -> {
                        empty.value = false
                    }
                    500 -> {
                        empty.value = false
                    }
                    else -> {
                        empty.value = false
                    }
                }
            }, {

            })
        compositeDisposable.add(disposable)
    }

    fun getParkingZones() {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getParkingZones(Methods.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                dataLoading.value = false
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        empty.value = false
                        parkingZones.value = data.body()?.parkingZones
                    }
                    404 -> {
                        empty.value = false
                    }
                    500 -> {
                        empty.value = false
                    }
                    else -> {
                        empty.value = false
                    }
                }
            }, {

            })
        compositeDisposable.add(disposable)
    }

    fun createOrder(createOrder: Create_order) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .createParkingOrder(Methods.getToken(), createOrder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        order.value = data.body()
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
                dataLoading.value = false
                empty.value = false
            })
        compositeDisposable.add(disposable)
    }

    fun checkStatus(text: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .checkParkingOrder(Methods.getToken(), text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println("status " + data.code())
                when (data.code()) {

                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        order.value = data.body()
                        println("order id: " + order.value?.orderId)
                        if (order.value?.orderId.isNullOrEmpty()) {
                            checkStatus(text)
                        }
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = false
                        if (order.value?.orderId.isNullOrEmpty()) {
                            checkStatus(text)
                        }

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

    fun getCredential(order_id: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiCapsFactory.instance.apiService
            .getCredentials(Methods.getToken(), order_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        credential.value = data.body()
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

    fun payWithNoSavedCars(view: View, order_id: String, checkout: Checkout) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiCapsFactory.instance.apiService
            .payWithNoSavedCard(Methods.getToken(), order_id, checkout)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        println(data.body())
                        paymentResult.value = data.body()
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                    400 -> {
                        dataLoading.value = false
                        empty.value = false
                        Snackbar.make(view, "Не хватает денег", Snackbar.LENGTH_LONG).show()
                    }
                    500 -> {
                        dataLoading.value = false
                        empty.value = false
                        Snackbar.make(view, "Произошла ошибка", Snackbar.LENGTH_LONG).show()

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

    fun payWithSavedCards(view: View, order_id: String, cardID: CardID) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiCapsFactory.instance.apiService
            .payWithSavedCard(Methods.getToken(), order_id, cardID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                println(data)

                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        paymentResult.value = data.body()
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                    400 -> {
                        dataLoading.value = false
                        empty.value = false
                        Snackbar.make(view, "Не хватает денег", Snackbar.LENGTH_LONG).show()
                    }
                    500 -> {
                        dataLoading.value = false
                        empty.value = false
                        Snackbar.make(view, "Произошла ошибка", Snackbar.LENGTH_LONG).show()

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
    fun threeDSFinish(view: View, threeDSFinish: ThreeDSFinish) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()

        val disposable = ApiCapsFactory.instance.apiService
            .threeDSFinish(Methods.getToken(), threeDSFinish)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        threeDS.value = true
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = false
                        threeDS.value = false

                    }
                    400 -> {
                        dataLoading.value = false
                        empty.value = false
                        Snackbar.make(view, "Не хватает денег", Snackbar.LENGTH_LONG).show()
                    }
                    500 -> {
                        dataLoading.value = false
                        empty.value = false
                        Snackbar.make(view, "Произошла ошибка", Snackbar.LENGTH_LONG).show()

                    }
                    else -> {
                        dataLoading.value = false
                        threeDS.value = false
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

    fun getSearchParkingZones(parkingZones: List<ParkingZone>, zone: String): List<ParkingZone> {
        val list: ArrayList<ParkingZone> = ArrayList()
        parkingZones.forEach {
            if (it.number.contains(zone)) list.add(it)
        }
        searchResultParkingZones = list
        return list
    }

}