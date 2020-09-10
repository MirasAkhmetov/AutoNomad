package com.autonomad.ui.claims.user_claims.home

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.Category
import com.autonomad.data.models.claim_user.Master
import com.autonomad.data.models.login.Message
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ClaimHomeViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var categories = MutableLiveData<List<Category>>()
    var masters = MutableLiveData<List<Master>>()
    var message = MutableLiveData<Message>()
    fun getCategories(token: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getCategories(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        categories.value = data.body()?.results
                        if (categories.value!!.isEmpty()) {
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

    fun getMasters(token: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getTopMasters(token)
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

//    fun deleteDriver(token: String, id: String, driver: Driver) {
//        compositeDisposable = CompositeDisposable()
//        val disposable = ApiPenaltyFactory.instance.apiService
//            .deleteDriver(token, id)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ data ->
//                when (data.code()) {
//                    200, 201 -> {
//                        Log.d("Tag", data.toString())
//                        message.value = data.body()
//                        val temp = (drivers.value as ArrayList<Driver>?)!!
//                        temp.remove(driver)
//                        drivers.value = temp
//                        if (drivers.value!!.isEmpty()) {
//                            empty.value = true;
//                        }
//                    }
//                    404 -> {
//                        empty.value = false
//                        dataLoading.value = false
//
//                    }
//                    500 -> {
//
//                    }
//                    else -> {
//
//                    }
//                }
//
//
//            }, { throwable ->
//                Log.d("Tag", throwable.toString())
//            })
//        compositeDisposable.add(disposable)
//    }
}