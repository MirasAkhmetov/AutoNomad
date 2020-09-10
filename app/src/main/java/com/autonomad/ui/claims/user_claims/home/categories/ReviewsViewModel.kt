package com.autonomad.ui.claims.user_claims.home.categories

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.Review
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ReviewsViewModel : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable

    var reviews = MutableLiveData<List<Review>>()

    fun getReviews(token: String, UIN: String) {
        dataLoading2.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getMastersReview(token, UIN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading2.value = false
                        empty3.value = false
                        reviews.value = data.body()?.resultss
                        if (reviews.value!!.isEmpty()) {
                            empty3.value = true
                        }
                    }
                    404 -> {
                        empty3.value = true
                    }
                    500 -> {
                        empty3.value = true
                    }
                    else -> {
                        empty3.value = true
                    }
                }
            }, {
                empty3.value = true
            })

        compositeDisposable.add(disposable)
    }
}