package com.autonomad.ui.check_auto.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.Page
import com.autonomad.data.models.addPage
import com.autonomad.data.models.check_auto.CheckAutoHistory
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.ui.check_auto.repo.CheckAutoHistoryRepo
import com.autonomad.ui.insurance.insurance_list.SerializableLiveData
import com.autonomad.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CheckAutoViewModel(private val repo: CheckAutoHistoryRepo) : DisposableViewModel() {
    var garageAutoList = SerializableLiveData<List<GarageCar>>()
    var ordersList = SerializableLiveData<List<CheckAutoHistory>>()
    var toast = SerializableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var carDeleted = MutableLiveData<Boolean>()

    fun loadGarageCars() {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getGarageCars()
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            garageAutoList.postValue(response.body()?.list)
                        }
                        404 -> {
                            toast.postValue("404 страница не найдена")
                        }
                        500 -> {
                            toast.postValue("500 ошибка сервера")
                        }
                        else -> {
                            toast.postValue("Неизвестная ошибка")
                        }
                    }
                } catch (e: Exception) {
                    toast.postValue("Нет интернет соединения")
                }
            }
        }
    }

    fun loadGarageCarsOrders() {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getOrders()
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            ordersList.postValue(response.body()?.list)
                        }
                        404 -> {
                            toast.postValue("404 страница не найдена")
                        }
                        500 -> {
                            toast.postValue("500 ошибка сервера")
                        }
                        else -> {
                            toast.postValue("Неизвестная ошибка")
                        }
                    }
                } catch (e: Exception) {
                    toast.postValue("Нет интернет соединения")
                }
            }
        }
    }

    fun deleteCar(carId: Int) {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.deleteCar(carId)
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            carDeleted.postValue(true)
                        }
                        404 -> {
                            toast.postValue("404 страница не найдена")
                        }
                        500 -> {
                            toast.postValue("500 ошибка сервера")
                        }
                        else -> {
                            toast.postValue("Неизвестная ошибка")
                        }
                    }
                } catch (e: Exception) {
                    toast.postValue("Нет интернет соединения")
                }
            }
        }
    }


    private val apiService = ApiGarageF.apiService

    private val mCheckAuto = MutableLiveData<Status<Page<GarageCar>>>()
    val checkAutoSearch: LiveData<Status<Page<GarageCar>>> = mCheckAuto

    private var page = 0

    fun search() {
        page = 0
        loadCars()
    }

    fun loadCars() {
        apiService.getGarageCars().subscribeAndDispose({
            if (mCheckAuto.addPage(page, it)) page++
        }, {
            Status.setFailed(mCheckAuto, it.localizedMessage)
        })
    }

}