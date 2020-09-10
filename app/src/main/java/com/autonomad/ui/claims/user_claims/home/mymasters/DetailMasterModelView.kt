package com.autonomad.ui.claims.user_claims.home.mymasters

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claim_user.*
import com.autonomad.utils.ApiAutoRegFactory
import com.autonomad.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailMasterModelView : BaseViewModel() {
    lateinit var compositeDisposable: CompositeDisposable
    var serviceImages = MutableLiveData<List<ServiceImage>>()
    var serviceOffers = MutableLiveData<List<Serviceoffers>>()
    var offers = MutableLiveData<List<Offers>>()

    //var category = MutableLiveData<List<Categorytwo>>()
    var mastersId = MutableLiveData<MastersId>()

    var favBoldima = MutableLiveData<Boolean>()

    var favjeke: Boolean = false

    var reviews = MutableLiveData<List<Review>>()
    var mastersReview = MutableLiveData<MastersReview>()

//    private val mProfile = MutableLiveData<Status<UserSpecialist>>()
//    val profile: LiveData<Status<UserSpecialist>>
//        get() = mProfile

    fun getMastersId(token: String, UIN: String) {
        println(UIN)
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .getMastersId(token, UIN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        //empty.value = false
                        //empty2.value = false
                        dataLoading.value = false
                        mastersId.value = data.body()
                        favjeke = data.body()?.is_fav!!
                        serviceImages.value = data.body()?.service_images
                        offers.value = data.body()?.offers
                        //serviceOffers.value = data.body()?.service_offers
                        serviceOffers.value = data.body()?.service_offers
                        //mProfile.value=data.body()?.service_offers
                        empty2.value = serviceOffers.value!!.isEmpty()
                        empty.value = serviceImages.value!!.isEmpty()
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
                dataLoading.value = false
                empty.value = false
            })
        compositeDisposable.add(disposable)
    }

    fun loadReviews(token: String, UIN: String) {
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
                        mastersReview.value = data.body()
                        reviews.value = data.body()?.resultss
                        if (reviews.value!!.isEmpty()) {
                            empty3.value = true;
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

    fun postFavour(token: String, id: String, mastID: MastID) { //
        compositeDisposable = CompositeDisposable()
        val disposable = ApiAutoRegFactory.instance.apiService
            .postFavour(token, mastID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        // message.value = data.body()
                        favBoldima.value = true

//                        if (subMasterList.value!!.isEmpty()) {
//                            empty.value = true;
//                        }
                    }
                    404 -> {
                        empty.value = false
                        dataLoading.value = false
                        favBoldima.value = false
                    }
                    500 -> {
                    }
                    else -> {
                        favBoldima.value = false
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
                        //message.value = data.body()

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