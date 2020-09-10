package com.autonomad.ui.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.login.City
import com.autonomad.data.models.login.CityUpdate
import com.autonomad.data.models.login.Profile
import com.autonomad.utils.*

class ShopCityViewModel : DisposableViewModel() {

    private val apiService = ApiCapsSingle.apiService

    private val mUser = MutableLiveData<Status<Profile>>()
    val user: LiveData<Status<Profile>> by lazy {
        loadUser()
        getCities()
        mUser
    }

    private fun loadUser() {
        apiService.getProfile().subscribeAndDispose(mUser::fromResponse, mUser::fromThrowable)
    }

    private val mCities = MutableLiveData<Status<Page<City>>>()
    val cities: LiveData<Status<Page<City>>> = mCities

    fun getCities(search: String = "") {
        apiService.getCities(search, 87, 0)
            .subscribeAndDispose(mCities::fromResponse, mCities::fromThrowable)
    }

    fun pickCity(city: Int) {
        val user = user.value?.item
        if (user == null) {
            mUser.value = "Не удалось выбрать город".fail
            return
        }
        apiService.updateCity(CityUpdate(city)).subscribeAndDispose({
            if (it.isSuccessful) loadUser()
        }, {
            val temp = mUser.value
            mUser.value = it.localizedMessage.fail
            mUser.value = temp
        })
    }
}