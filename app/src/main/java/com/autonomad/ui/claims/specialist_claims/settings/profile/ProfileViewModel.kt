package com.autonomad.ui.claims.specialist_claims.settings.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.claims.CreateOffer
import com.autonomad.data.models.claims.Review
import com.autonomad.data.models.claims.ServiceOffer
import com.autonomad.data.models.claims.Specialist
import com.autonomad.utils.*
import retrofit2.Response

class ProfileViewModel : DisposableViewModel() {
    private val apiService = ApiRequestsFactory.apiService

    private val mProfile = MutableLiveData<Status<Specialist>>()
    val profile: LiveData<Status<Specialist>>
        get() = mProfile

    private val mReviews = MutableLiveData<Status<Page<Review>>>()
    val reviews: LiveData<Status<Page<Review>>>
        get() = mReviews

    private val onComplete: (Response<*>) -> Unit = {
        if (it.isSuccessful) loadData()
        else mProfile.value = (it.errorBody()?.string() ?: it.message()).fail
    }
    private val onError: (Throwable) -> Unit = { mProfile.value = it.localizedMessage.fail }

    fun loadData() {
        mProfile.value = Status.loading
        apiService.logSpecialist().subscribeOnIo().subscribe({ response ->
            val result = response.toStatus()
            result?.onSuccess { loadReviews(id) }
            mProfile.value = result
        }, onError).disposeOnCleared()
    }

    fun delete(service: ServiceOffer) {
        apiService.deleteServiceOffer(service.id).subscribeOnIo().subscribe(onComplete, onError).disposeOnCleared()
    }

    fun update(service: ServiceOffer) {
        apiService.updateServiceOffer(service.id, CreateOffer.fromServiceOffer(service, true)).subscribeOnIo()
            .subscribe(onComplete, onError)
            .disposeOnCleared()
    }

    fun deleteImage(id: Int) {
        apiService.deleteImage(id).subscribeOnIo().subscribe(onComplete, onError).disposeOnCleared()
    }

    private fun loadReviews(masterId: Int) {
        apiService.getReviews(masterId, 3, 0).subscribeOnIo().subscribe({
            mReviews.value = it.toStatus()
        }, {
            Log.d("ProfileViewModelLogcat", "loadReviews: throwable $it")
            mReviews.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}