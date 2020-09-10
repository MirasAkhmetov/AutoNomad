package com.autonomad.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.autonomad.data.models.HTTPStatus
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class BaseViewModel : ViewModel() {

    val empty = MutableLiveData<Boolean>().apply { value = null }
    val empty2 = MutableLiveData<Boolean>().apply { value = null }
    val empty3 = MutableLiveData<Boolean>().apply { value = null }

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading2 = MutableLiveData<Boolean>().apply { value = false }

    val httpStatus = MutableLiveData<HTTPStatus>().apply { value = HTTPStatus(200) }

    protected val disposable = CompositeDisposable()

    protected fun Disposable.disposeOnCleared() = disposable.apply { add(this@disposeOnCleared) }

    protected fun <T> Single<T>.subscribeOnIo(): Single<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    protected fun <T> Observable<T>.subscribeOnIo(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    protected fun <T> Single<T>.subscribeAndDispose(onSuccess: (T) -> Unit, onFailure: (Throwable) -> Unit) {
        subscribeOnIo().subscribe(onSuccess, onFailure).disposeOnCleared()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}