package com.autonomad.ui.profile.settings.addresses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Address
import com.autonomad.utils.*

class AddressesViewModel : DisposableViewModel() {
    private val apiService = ApiCapsSingle.apiService

    private val mAddresses = MutableLiveData<Status<List<Address>>>()
    val addresses: LiveData<Status<List<Address>>> by lazy {
        loadAddresses()
        mAddresses
    }

    private val mMessage = MutableLiveData<String>()
    val message: LiveData<String> = mMessage

    private fun loadAddresses() {
        mAddresses.value = Status.loading
        apiService.getAddresses().subscribeAndDispose({
            mAddresses.value = it.toStatus()
        }, {
            mAddresses.value = it.localizedMessage.fail
        })
    }

    fun deleteAddress(id: Int) {
        apiService.deleteAddress(id).subscribeAndDispose({
            if (it.isSuccessful) {
                mMessage.value = "Адрес успешно удалён"
                val item = (mAddresses.value?.item as? MutableList) ?: mutableListOf()
                item.removeAll { address -> address.id == id }
                mAddresses.value = item.success
                loadAddresses()
            }
            else mMessage.value = it.message()
        }, {
            mMessage.value = it.localizedMessage
        })
    }
}