package com.autonomad.ui.bottom_sheet.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Address
import com.autonomad.utils.*

class AddressesViewModel : DisposableViewModel() {
    val apiService = ApiCapsSingle.apiService

    private val mAddresses = MutableLiveData<Status<List<Address>>>()
    val addresses: LiveData<Status<List<Address>>> by lazy {
        loadAddresses()
        mAddresses
    }

    private fun loadAddresses() {
        mAddresses.value = Status.loading
        apiService.getAddresses().subscribeAndDispose(mAddresses::fromResponse, mAddresses::fromThrowable)
    }
}