package com.autonomad.ui.claims.user_claims.home.mycategory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.CategoriesId
import com.autonomad.data.models.claim_user.SubCategory
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class CategoryListViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var subcategory = MutableLiveData<List<SubCategory>>()
    var sub_name = MutableLiveData<CategoriesId>()
    fun getCategoryId(token: String, idd: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getCategoriesId(token, idd)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        Log.d("loogg1", data.toString())
                        empty.value = false
                        dataLoading.value = false
                        sub_name.value = data.body()
                        //sub_name.value = data.body()?.name
                        subcategory.value = data.body()?.results
                        //subcategory2 = data.body()?.results?.let { arrayListOf(it) }!!
                        if (subcategory.value!!.isEmpty()) {
                            empty.value = true
                        }
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }
            }, { throwable ->
                Log.d("Tag", throwable.toString())
                dataLoading.value = false
                empty.value = false
            })
        compositeDisposable.add(disposable)
    }
}