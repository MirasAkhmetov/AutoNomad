package com.autonomad.ui.bottom_sheet.penalty

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claims.MasterComplaintRequest
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.Methods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MasterComplaintViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var masterComplaint = MutableLiveData<MasterComplaintRequest>()

    fun complaintMaster(complaintModel: MasterComplaintRequest) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .complaintMaster(Methods.getToken(), complaintModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        masterComplaint.value = data.body()
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
            }, { throwable ->
                dataLoading.value = false
                empty.value = true
            })
        compositeDisposable.add(disposable)
    }
}