package com.autonomad.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.login.Profile
import com.autonomad.utils.ApiCapsFactory
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.Methods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable

    var profile = MutableLiveData<Profile>()


    fun getProfile(token: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiCapsFactory.instance.apiService
            .getProfile(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                if (data.isSuccessful) Methods.name = data.body()?.firstName
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        Log.d("prof", data.body().toString())
                        //empty.value = false
//                        dataLoading.value = false
                        profile.value = data.body()

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


}