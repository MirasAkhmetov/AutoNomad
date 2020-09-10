package com.autonomad.ui.main_page.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.data.models.login.Profile
import com.autonomad.data.models.main_page_car.AvgPrice
import com.autonomad.data.models.main_page_car.Inspection
import com.autonomad.data.models.main_page_car.SrtsUpdate
import com.autonomad.data.models.meteo_currency.City
import com.autonomad.data.models.meteo_currency.Currency
import com.autonomad.data.models.meteo_currency.Weather
import com.autonomad.data.models.penalty.Driver
import com.autonomad.network.ApiCaps
import com.autonomad.network.ApiGarage
import com.autonomad.network.ApiMeteoCurrency
import com.autonomad.utils.*
import io.reactivex.Observable

class MainPageViewModel(
    private val apiGarage: ApiGarage,
    private val apiMeteoCurrency: ApiMeteoCurrency,
    private val apiCaps: ApiCaps
) : DisposableViewModel() {

    var isConnected = MutableLiveData(true)

    private val mLoading = MutableLiveData(true)
    val loading: LiveData<Boolean> = mLoading

    private val mProfile = MutableLiveData<Status<Profile>>()
    val profile: LiveData<Status<Profile>> = mProfile

    fun loadData(forceLoad: Boolean = false) {
        loadDrivers()
        if (forceLoad || profile.value == null) loadUser()
        if (forceLoad || garageCars.value == null) loadCars()
        if (forceLoad || insurance.value == null) loadInsurance()
        if (forceLoad || inspections.value == null) loadInspections()
        if (forceLoad || rub.value == null) loadCurrency()
    }

    private fun loadUser() {
        apiCaps.getProfile().subscribeAndDispose({
            if (it.isSuccessful) Methods.name = it.body()?.firstName
            mProfile.value = it.toStatus()
            val cityId = it.body()?.city?.id
            if (cityId != null)
                loadFuel(cityId)
        }, {
            loadFuel(-1)
            mProfile.fromThrowable(it)
        })
    }

    private val mGarageCars = MutableLiveData<Status<List<GarageCar>>>()
    val garageCars: LiveData<Status<List<GarageCar>>> = mGarageCars

    private fun loadCars() {
        apiGarage.getGarageCars().subscribeAndDispose({
            mLoading.value = false
            mGarageCars.value = it.listToStatus()
        }, {
            mLoading.value = false
            mGarageCars.value = it.localizedMessage?.fail
        })
    }

    private val mInsurance = MutableLiveData<Status<List<InsuranceHistory>>>()
    val insurance: LiveData<Status<List<InsuranceHistory>>> = mInsurance

    private fun loadInsurance() {
        mInsurance.value = Status.loading
        apiGarage.getInsuranceFavorite().subscribeAndDispose({
            mLoading.value = false
            mInsurance.value = it.listToStatus()
        }, mInsurance::fromThrowable)
    }

    private val mDrivers = MutableLiveData<Status<List<Driver>>>()
    val drivers: LiveData<Status<List<Driver>>> = mDrivers

    private fun loadDrivers() {
        mDrivers.value = Status.loading
        apiGarage.getDrivers().subscribeAndDispose({
            mLoading.value = false
            mDrivers.value = it.listToStatus()
        }, mDrivers::fromThrowable)
    }

    private val mInspections = MutableLiveData<Status<List<Inspection>>>()
    val inspections: LiveData<Status<List<Inspection>>> = mInspections

    private fun loadInspections() {
        mInspections.value = Status.loading
        apiGarage.getInspections().subscribeAndDispose({
            mLoading.value = false
            mInspections.value = it.listToStatus()
        }, mInspections::fromThrowable)
    }

    private val mUpdateSrts = MutableLiveData<Status<Int>>()
    val updateSrts: LiveData<Status<Int>> = mUpdateSrts

    fun updateCar(car: GarageCar, srts: String): LiveData<Status<Int>> {
        apiGarage.updateSrts(car.id, SrtsUpdate(srts)).subscribeAndDispose({
            mUpdateSrts.value = if (it.isSuccessful) car.id.success else (it.message() ?: it.errorBody()?.string()).fail
        }, mUpdateSrts::fromThrowable)
        return updateSrts
    }

    val rub = MutableLiveData<Currency>()
    val eur = MutableLiveData<Currency>()
    val usd = MutableLiveData<Currency>()

    private fun loadCurrency() {
        apiMeteoCurrency.getCurrency().subscribeAndDispose({
            if (it.isSuccessful) {
                rub.value = it.body()?.message?.firstOrNull { currency -> currency.title == "RUB" }
                eur.value = it.body()?.message?.firstOrNull { currency -> currency.title == "EUR" }
                usd.value = it.body()?.message?.firstOrNull { currency -> currency.title == "USD" }
            }
        }, {
            it.printStackTrace()
        })
    }

    private val mWeather = MutableLiveData<Status<Weather>>()
    val weather: LiveData<Status<Weather>> = mWeather

    private val mFuel = MutableLiveData<Status<City>>()
    val fuel: LiveData<Status<City>> = mFuel

    fun setLocation(latitude: Double, longitude: Double) {
        Methods.saveLocation(latitude, longitude)
        apiMeteoCurrency.getWeather(latitude, longitude).subscribeAndDispose(mWeather::fromResponse, mWeather::fromThrowable)
    }

    private fun loadFuel(cityId: Int) {
        apiMeteoCurrency.getFuelNearest(cityId).subscribeOnIo()
            .subscribeAndDispose(mFuel::fromResponse, mFuel::fromThrowable)
    }

    private val mAvgPrice = MutableLiveData<Status<List<AvgPrice>>>()
    val avgPrice: LiveData<Status<List<AvgPrice>>> = mAvgPrice

    private fun getPrice(cars: List<GarageCar>) {
        val priceList = mutableListOf<AvgPrice>()
        Observable.fromIterable(cars).flatMap { apiGarage.getAvgPrice(it.stateNumber).toObservable() }.subscribeAndDispose({
            priceList.add(
                if (it.body()?.price != null && it.body()?.price != "No id information is provided for cars brand or model")
                    it.body()!!
                else AvgPrice(it.body()?.vin.orEmpty(), null)
            )
        }, mAvgPrice::fromThrowable, {
            mAvgPrice.value = priceList.success
        })
    }
}