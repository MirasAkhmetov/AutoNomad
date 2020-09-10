package com.autonomad.ui.check_auto.report

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.payment.*
import com.autonomad.data.models.payment.BankingCard
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.ApiCapsFactory
import com.autonomad.utils.Methods
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PaymentViewModel : BaseViewModel() {
    var credential = MutableLiveData<Credentials>()
    var paymentResult = MutableLiveData<PaymentResult>()
    var threeDS = MutableLiveData<Boolean>()
    var bankingCards = MutableLiveData<BankingCard>()

    lateinit var compositeDisposable: CompositeDisposable

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

    fun getBankingCards() {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiCapsFactory.instance.apiService
            .getBankingCards(Methods.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        bankingCards.value=data.body()
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