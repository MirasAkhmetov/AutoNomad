package com.autonomad.ui.claims.user_claims.claims.detail_claims

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.ClaimMasterResult
import com.autonomad.data.models.claims.ServiceModel
import com.autonomad.data.models.main_page_car.IsActivUpdate
import com.autonomad.data.models.penalty.Result
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailClaimViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var result = MutableLiveData<List<Result>>()
    var servicess = MutableLiveData<ServiceModel>()
    fun getServId(token: String, UIN: String) {
        println(UIN)
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getServiceReqId(token, UIN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        servicess.value = data.body()
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }
            }, { throwable ->
                dataLoading.value = false
                empty.value = false
            })
        compositeDisposable.add(disposable)
    }


    var favtwo = MutableLiveData<Boolean>()
    fun deleteSer(token: String, id: String) {
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .deleteServicereq(token, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        //message.value = data.body()
                        favtwo.value = true

                    }
                    404 -> {
                        dataLoading.value = false
                        favtwo.value = false

                    }
                    500 -> {
                        favtwo.value = false
                    }
                    else -> {
                        favtwo.value = false
                    }
                }


            }, { throwable ->
            })
        compositeDisposable.add(disposable)
    }

    var favBoldima = MutableLiveData<Boolean>()
    fun patchIsActiv(token: String, id: String, isMain: IsActivUpdate) { //
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .patchIsActiv(token, id.toInt(), isMain)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        favBoldima.value = true
                    }
                    404 -> {
                        dataLoading.value = false
                        favBoldima.value = false
                    }
                    500 -> {
                        favBoldima.value = false
                    }
                    else -> {
                        favBoldima.value = false
                    }
                }


            }, { throwable ->
            })
        compositeDisposable.add(disposable)
    }

    var masters = MutableLiveData<List<ClaimMasterResult>>()
    fun getMasters(token: String,id: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getMastersRes(token,id )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        masters.value = data.body()?.results
                        if (masters.value!!.isEmpty()) {
                            empty.value = true;
                        }
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = true
                    }
                    500 -> {
                        dataLoading.value = false
                        empty.value = true
                    }
                    else -> {
                        dataLoading.value = false
                        empty.value = true
                    }
                }


            }, {
                dataLoading.value = false
                empty.value = true
            })
        compositeDisposable.add(disposable)
    }
}