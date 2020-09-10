package com.autonomad.ui.claims.user_claims.claims

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.Services
import com.autonomad.data.models.claim_user.Servicesreq
import com.autonomad.data.models.login.Message
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ClaimsViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var services = MutableLiveData<List<Services>>()
    var servicesreq = MutableLiveData<Servicesreq>()
    //var masters = MutableLiveData<List<Master>>()
    var message = MutableLiveData<Message>()
    fun getServicereq(token: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getServicereq(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        servicesreq.value = data.body()
                        services.value = data.body()?.results
                        if (services.value!!.isEmpty()) {
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


            }, {

            })

        compositeDisposable.add(disposable)
    }

    fun deleteServicereq(token: String, id: String, service: Services) {
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .deleteServicereq(token, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        Log.d("Tag", data.toString())
                        message.value = data.body()
                        val temp = (services.value as ArrayList<Services>?)!!
                        temp.remove(service)
                        services.value = temp
                        if (services.value!!.isEmpty()) {
                            empty.value = true;
                        }
                    }
                    404 -> {
                        empty.value = false
                        dataLoading.value = false

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
}