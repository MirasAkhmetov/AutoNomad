package com.autonomad.ui.profile.change_pass

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.login.Password
import com.autonomad.utils.ApiCapsFactory
import com.autonomad.utils.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ChangePassViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var success = MutableLiveData<Boolean>()
    fun changePassword(view: View, password: Password, token: String) {
        Log.d("logiin", password.toString())
        Log.d("logiin", token.toString())
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiCapsFactory.instance.apiService
            .changePassword(password, token) //"Token $token"
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        success.value = true
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = false
                        success.value = false
                        Snackbar.make(view, "Ошибка 404", Snackbar.LENGTH_LONG)
                            .show()


                    }
                    500 -> {
                        dataLoading.value = false
                        empty.value = false

                        success.value = false
                        Snackbar.make(view, "Ошибка 500", Snackbar.LENGTH_LONG)
                            .show()

                    }
                    else -> {
                        Snackbar.make(view, "Ошибка " + data.code(), Snackbar.LENGTH_LONG)
                            .show()
                        dataLoading.value = false
                        success.value = false
                        empty.value = false

                    }
                }

            }, { throwable ->
                Snackbar.make(view, throwable.toString(), Snackbar.LENGTH_LONG)
                    .show()

                dataLoading.value = false
                success.value = false
                empty.value = false
            })
        compositeDisposable.add(disposable)
    }
}