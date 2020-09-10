package com.autonomad.ui.claims.user_claims.claims.claimfour

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.data.models.claim_user.ServiceAdreska
import com.autonomad.data.models.claim_user.ServiceCreate
import com.autonomad.data.models.main_page_car.Result
import com.autonomad.utils.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ClaimFourViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable

    private val apiService = ApiGarageFactory.instance.apiService
    var cars = MutableLiveData<List<Result>>()

    fun getCars(token: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = apiService
            .getCars(token)
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

            })

        compositeDisposable.add(disposable)
    }

    var favBoldima = MutableLiveData<Boolean>()
    fun sendCreate(serviceCreate: ServiceCreate) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .serviceCreate(Methods.getToken(), serviceCreate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                dataLoading.value = false
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        favBoldima.value = true
                    }
                    404 -> {
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
                dataLoading.value = false

            })
        compositeDisposable.add(disposable)
    }

    fun editCreate(claimId: Int, serviceCreate: ServiceCreate) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .editCreate(Methods.getToken(), claimId, serviceCreate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                dataLoading.value = false
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        favBoldima.value = true
                    }
                    404 -> {
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
                dataLoading.value = false

            })
        compositeDisposable.add(disposable)
    }

    private val apiService1 = ApiGarageF.apiService
    private val mCheckAuto = MutableLiveData<Status<List<GarageCar>>>()
    val checkAutoSearch: LiveData<Status<List<GarageCar>>> = mCheckAuto
    fun search() {
        apiService1.getGarageCars().subscribeOnIo().subscribe({
            mCheckAuto.value = it.listToStatus()
        }, {
            mCheckAuto.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }


}