package com.autonomad.ui.claims.specialist_claims.settings.add_offer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claims.Category
import com.autonomad.data.models.claims.CreateOffer
import com.autonomad.data.models.claims.ServiceOffer
import com.autonomad.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddOfferViewModel : DisposableViewModel() {
    private val apiService = ApiRequestsFactory.apiService

    private val _categories = MutableLiveData<Status<List<Category>>>()
    val categories: LiveData<Status<List<Category>>> by lazy {
        loadData()
        _categories
    }

    private fun loadData() {
        apiService.getCategories().subscribeAndDispose({
            _categories.value = it.listToStatus()
            if (it.isSuccessful) getSubcategories(it.body()!!.list)
        }, {
            _categories.value = it.localizedMessage.fail
        })
    }

    private fun getSubcategories(oldList: List<Category>) {
        val newList = mutableListOf<Category>()
        for (category in oldList) {
            apiService.getCategoryById(category.id).subscribeAndDispose({
                if (it.isSuccessful) {
                    newList.add(category.copy(subcategories = it.body()?.subcategories))
                    if (newList.size == oldList.size)
                        _categories.value = newList.success
                }
            }, {
                _categories.value = it.localizedMessage.fail
            })
        }
    }

    fun save(offers: List<ServiceOffer>) {
        _categories.value = Status.loading
        for (offer in offers) {
            val d = apiService.addOffers(CreateOffer.fromServiceOffer(offer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _categories.value = if (it.isSuccessful) Status.idle else (it.errorBody()?.string() ?: it.message()).fail
                }, {
                    _categories.value = it.localizedMessage.fail
                })
            disposable.add(d)
        }
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}