package com.autonomad.ui.claims.specialist_claims.home.repair_auto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.Page
import com.autonomad.data.models.addPage
import com.autonomad.data.models.claims.ServiceRequest
import com.autonomad.utils.ApiRequestsFactory
import com.autonomad.utils.DisposableViewModel
import com.autonomad.utils.Status

class RepairAutoViewModel(private val subcategoryId: Int) : DisposableViewModel() {
    private val apiService = ApiRequestsFactory.apiService

    var filter: Int = -1
        private set
    private var page = 0

    private val mRequests = MutableLiveData<Status<Page<ServiceRequest>>>()
    val requests: LiveData<Status<Page<ServiceRequest>>> = mRequests

    fun getRequests(f: Int = filter, latitude: String? = null, longitude: String? = null) {
        if (filter != f) {
            filter = f
            page = 0
        }
        apiService.getServiceRequestsWithFilter(
            limit = 20,
            offset = 20 * (page),
            subcategory = if (subcategoryId > 0) subcategoryId else null,
            byNewest = if (f == FilterDialog.NEW) true else null,
            priceUp = if (f == FilterDialog.PRICE_DEC) "price_up" else null,
            priceDown = if (f == FilterDialog.PRICE_INC) "price_down" else null,
            latitude = latitude,
            longitude = longitude
        ).subscribeAndDispose({
            if (mRequests.addPage(page, it)) page++
        }, {
            Status.setFailed(mRequests, it.localizedMessage)
        })
    }
}

@Suppress("UNCHECKED_CAST")
class RepairAutoViewModelProvider(private val subcategoryId: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = RepairAutoViewModel(subcategoryId) as T
}