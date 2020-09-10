package com.autonomad.ui.claims.user_claims.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.*
import com.autonomad.data.models.login.Message
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavouritesViewModel: BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable

    var subMasterList = MutableLiveData<List<Master>>()
    var subMaster = MutableLiveData<Masters>()
    var message = MutableLiveData<Message>()

    var favBoldima= MutableLiveData<Boolean>()
    private var filter: Int = -1

    //    fun filterData(f: Int = filter) {
//        filter = f
//        val data = subMasterList.value
//        data?.onSuccess {
//            if (filter != -1) {
//                var temp = list
//                temp = when (filter) {
//                    FilterDialog.NEW -> temp.sortedBy { it.startDay.orEmpty() }
//                    FilterDialog.PRICE_INC -> temp.sortedBy { it.budget }
//                    FilterDialog.PRICE_DEC -> temp.sortedByDescending { it.budget }
//                    FilterDialog.NEAR -> temp.sortedBy { it.address.id }
//                    else -> temp
//                }
//                _requests.value = this.copy(list = temp).success
//            }
//        }
//    }
    fun getFubCatt(token: String, UIN: String){
        println(UIN)
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getFubCat(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->

                when (data.code()) {
                    200, 201 -> {
                        Log.d("qwerty1", data.toString())
                        empty.value = false
                        dataLoading.value = false
                        subMaster.value = data.body()
                        subMasterList.value = data.body()?.results

                        if (subMasterList.value!!.isEmpty()){
                            empty.value=true
                        }

//                        if (data.body()?.error!!) {
//                            empty.value = true
//                        } else {
//                            val setOfPenalties = ArrayList<Result>()
//                            data.body()?.result!!.forEach { i ->
//                                i.result.forEach { j ->
//                                    setOfPenalties.add(j)
//                                }
//                            }
//                            println(setOfPenalties)
//                            result.value = setOfPenalties
//                        }
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }
            }, { throwable ->
                Log.d("Tagi", throwable.toString())
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
                        Log.d("Tagii200", data.toString())
                        // message.value = data.body()
                        favBoldima.value=true

                        if (subMasterList.value!!.isEmpty()) {
                            empty.value = true;
                        }
                    }
                    404 -> {
                        Log.d("Tagii404", data.toString())
                        empty.value = false
                        dataLoading.value = false
                        favBoldima.value=false
                    }
                    500 -> {
                        Log.d("Tagii500", data.toString())
                    }
                    else -> {
                        Log.d("Tagiielse", data.toString())
                        favBoldima.value=false
                    }
                }


            }, { throwable ->
                Log.d("Tag", throwable.toString())
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
                        Log.d("Tagii200", data.toString())
                        //message.value = data.body()

                    }
                    404 -> {
                        Log.d("Tagii404", data.toString())
                        empty.value = false
                        dataLoading.value = false

                    }
                    500 -> {
                        Log.d("Tagii500", data.toString())
                    }
                    else -> {
                        Log.d("Tagiielse", data.toString())
                    }
                }


            }, { throwable ->
                Log.d("Tag", throwable.toString())
            })
        compositeDisposable.add(disposable)
    }

}