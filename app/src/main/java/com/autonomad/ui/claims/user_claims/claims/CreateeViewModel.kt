package com.autonomad.ui.claims.user_claims.claims

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.Category
import com.autonomad.data.models.claim_user.Master
import com.autonomad.data.models.claim_user.createInfo
import com.autonomad.data.models.login.Message
import com.autonomad.data.models.main_page_car.Car
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreateeViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    val createInfo = MutableLiveData<createInfo>()
    var data = MutableLiveData<Car>()


        fun initialise() {
        createInfo.value = createInfo(
            "",
            ""
        )
    }

    fun setData(createInfo: createInfo) {
        Log.d("asdfg3", createInfo.toString())
        this.createInfo.value = createInfo
    }
    fun setData(car: Car) {
        data.value = car
    }
//lateinit var compositeDisposable: CompositeDisposable
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

}