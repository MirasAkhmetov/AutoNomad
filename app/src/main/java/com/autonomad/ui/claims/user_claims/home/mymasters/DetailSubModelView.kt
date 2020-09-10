package com.autonomad.ui.claims.user_claims.home.mymasters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.*
import com.autonomad.data.models.login.Message
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailSubModelView: BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable

    var subMasterList = MutableLiveData<List<Master>>()
    var subMaster = MutableLiveData<Masters>()
    var message = MutableLiveData<Message>()

    var favBoldima= MutableLiveData<Boolean>()

    fun getMasters(token: String, subcategoryId: Int,
                   sortByRating: Boolean? = null,
                   sortByPopularity: Boolean? = null ) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getMastersClaims(token, subcategoryId, sortByRating, sortByPopularity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        subMaster.value = data.body()
                        subMasterList.value = data.body()?.results
                        if (subMasterList.value!!.isEmpty()) {
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
                dataLoading.value = false
                empty.value = false
            })
        compositeDisposable.add(disposable)
    }

    fun postFavour(token: String, id: String, mastID: MastID) {
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .postFavour(token,  mastID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        favBoldima.value=true

                        if (subMasterList.value!!.isEmpty()) {
                            empty.value = true;
                        }
                    }
                    404 -> {
                        empty.value = false
                        dataLoading.value = false
                        favBoldima.value=false
                    }
                    500 -> {
                    }
                    else -> {
                        favBoldima.value=false
                    }
                }


            }, { throwable ->
            })
        compositeDisposable.add(disposable)
    }

    fun deleteFavour(token: String, id: String) {
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .deleteFavour(token, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
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
            })
        compositeDisposable.add(disposable)
    }

}