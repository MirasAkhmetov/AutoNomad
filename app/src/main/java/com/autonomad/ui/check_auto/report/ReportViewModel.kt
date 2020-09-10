package com.autonomad.ui.check_auto.report

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.check_auto.CreateOrder
import com.autonomad.data.models.check_auto.CreateOrderStatus
import com.autonomad.data.models.check_auto.DetailTicket
import com.autonomad.ui.check_auto.repo.CheckAutoHistoryRepo
import com.autonomad.utils.DisposableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ReportViewModel(private val repo: CheckAutoHistoryRepo) : DisposableViewModel() {
    var order = MutableLiveData<CreateOrderStatus>()

    val detailTicket = MutableLiveData<DetailTicket>()
    val carInfo = MutableLiveData<com.autonomad.data.models.check_auto.Report>()
    val loading = MutableLiveData<Boolean>()
    var toast = MutableLiveData<String>()

    val dtpState = MutableLiveData<CheckState>()
    val historyState = MutableLiveData<CheckState>()

    fun loadData(vin: String?, id: Int?) {
        when {
            vin != null -> loadVin(vin)
            id != null -> loadOrder(id, true)
            else -> toast.postValue("Не указаны данные")
        }
    }

    private fun loadVin(vin: String) {
        Log.d("madiyar", "vin = $vin")
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getCarByVinDetail(vin)
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            carInfo.postValue(response.body())
                            searchOrder(vin)
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

    private fun loadOrder(id: Int, loadVin: Boolean = false) {
        Log.d("madiyar", "loadOrder = $id")
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getCheckAutoDetailTicket(id)
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            detailTicket.postValue(response.body())
                            if (loadVin && response.body()?.report != null) {
                                val detailTicket = response.body()!!
                                loadVin(detailTicket.report.car_info.vin_number)
                            }
                            val report = response.body()?.report
                            if (report != null) {
                                if (report.car_dtps.isNotEmpty())
                                    dtpState.postValue(CheckState.BOUGHT)
                                else
                                    dtpState.postValue(CheckState.EMPTY)
                                if (report.car_histories.isNotEmpty())
                                    historyState.postValue(CheckState.BOUGHT)
                                else
                                    historyState.postValue(CheckState.EMPTY)
                            }
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

    private fun searchOrder(vin: String) {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getOrders()
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            var orderFound = false
                            response.body()?.list?.forEach {
                                if (it.vehicle?.vin_number == vin) {
                                    orderFound = true
                                    loadOrder(it.id)
                                }
                            }
                            val report = carInfo.value
                            if (!orderFound && report != null) {
                                if (report.dtp_number > 0) dtpState.postValue(CheckState.EXISTS)
                                else dtpState.postValue(CheckState.EMPTY)
                                if (report.history_number > 0) historyState.postValue(CheckState.EXISTS)
                                else historyState.postValue(CheckState.EMPTY)
                            }
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

    fun createOrder(createOrder: CreateOrder) {
        println(createOrder)
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.createOrder(createOrder)
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            order.postValue(response.body())
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

    fun checkStatus(id: Int) {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.checkOrderStatus(id)
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            order.postValue(response.body())
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
}

enum class CheckState {
    EMPTY, EXISTS, BOUGHT
}
