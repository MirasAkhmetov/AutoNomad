package com.autonomad.ui.bottom_sheet.banking_cards

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.payment.BankingCardResult
import com.autonomad.utils.ApiCapsFactory
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.Methods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BankingCardViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var bankingCards = MutableLiveData<List<BankingCardResult>>()

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
                        bankingCards.value = data.body()?.result
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
