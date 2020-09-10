package com.autonomad.utils

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class DisposableViewModel : ViewModel() {
    protected val disposable = CompositeDisposable()

    protected fun Disposable.disposeOnCleared() = disposable.apply { add(this@disposeOnCleared) }

    protected fun <T> Single<T>.subscribeOnIo(): Single<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    protected fun <T> Observable<T>.subscribeOnIo(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    protected fun <T> Single<T>.subscribeAndDispose(onSuccess: (T) -> Unit, onFailure: (Throwable) -> Unit) {
        subscribeOnIo().subscribe(onSuccess, onFailure).disposeOnCleared()
    }

    protected fun <T> Observable<T>.subscribeAndDispose(
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        subscribeOnIo().subscribe(onSuccess, onFailure, onComplete).disposeOnCleared()
    }

    override fun onCleared() {
        disposable.dispose()
        timber("onCleared")
        super.onCleared()
    }
}