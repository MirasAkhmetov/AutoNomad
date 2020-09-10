package com.autonomad.ui.profile.settings.addresses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.*
import com.autonomad.data.models.login.City
import com.autonomad.utils.*
import com.autonomad.utils.Status

class EditAddressViewModel(private val addressId: Int?) : DisposableViewModel() {
    private val apiCaps = ApiCapsSingle.apiService

    private val mAddress = MutableLiveData<Status<Address>>()
    val address: LiveData<Status<Address>> by lazy {
        checkAddress()
        mAddress
    }

    private val mCity = MutableLiveData<Status<String>>()
    val city: LiveData<Status<String>> by lazy {
        loadCity()
        mCity
    }

    private val mCities = MutableLiveData<Status<Page<City>>>()
    val cities: LiveData<Status<Page<City>>> = mCities

    private val mResult = MutableLiveData<Status<Boolean>>()
    val result: LiveData<Status<Boolean>> = mResult

    private fun checkAddress() {
        if (addressId == null) return
        mAddress.value = Status.loading
        apiCaps.getAddress(addressId).subscribeAndDispose({
            mAddress.value = it.toStatus()
        }, {
            mAddress.value = it.localizedMessage.fail
        })
    }

    private fun loadCity() {
        mCity.value = Status.loading
        apiCaps.getProfile().subscribeAndDispose({
            if (it.isSuccessful) Methods.name = it.body()?.firstName
            mCity.value = it.body()?.city?.name.success ?: Status.idle
        }, mCity::fromThrowable)
    }

    private var page = 0
    private var lastSearch = ""
    fun loadCities(search: String) {
        if (search != lastSearch) {
            page = 0
            lastSearch = search
        }
        apiCaps.getCities(search, 20, page * 20).subscribeAndDispose({
            if (mCities.addPage(page, it)) page++
        }, {
            Status.setFailed(mCities, it.localizedMessage)
        })
    }

    fun saveAddress(title: String, city: String, street: String, house: String, flat: String) {
        mResult.value = Status.loading
        apiCaps.getCities(city, 1, 0).subscribeAndDispose({
            val c = it.body()?.list?.firstOrNull()
            if (c != null) {
                val cityId = c.id
                val countryId = c.country
                val type = AddressType.values().firstOrNull { t -> t.title?.toLowerCase() == title.toLowerCase().trim() }
                val address = AddressCreate(
                    latitude = c.latitude.orEmpty(),
                    longitude = c.longitude.orEmpty(),
                    description = if (type == null) title.trim() else null,
                    country = countryId,
                    city = cityId,
                    extra = "$street $house, кв $flat",
                    type = (type ?: AddressType.FrequentlyUsed).string
                )
                val d = if (addressId == null) apiCaps.addAddress(address) else apiCaps.updateAddress(addressId, address)
                d.subscribeAndDispose({ response ->
                    mResult.value = if (response.isSuccessful) true.success else response.message().fail
                }, mResult::fromThrowable)
            } else mResult.value = it.message().fail
        }, mResult::fromThrowable)
    }
}

@Suppress("UNCHECKED_CAST")
class EditAddressFactory(private val id: Int?) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = EditAddressViewModel(id) as T
}