package com.autonomad.ui.check_auto.receipt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.check_auto.DetailTicket
import com.autonomad.ui.check_auto.repo.CheckAutoHistoryRepo
import com.autonomad.ui.insurance.insurance_list.SerializableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class DetailReceiptViewModel(val repo: CheckAutoHistoryRepo) : ViewModel() {
    var detailsTicket = MutableLiveData<DetailTicket>()
    var toast = SerializableLiveData<String>()
    var loading = MutableLiveData<Boolean>()

    fun getData(id: Int) {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getCheckAutoDetailTicket(id)
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            detailsTicket.postValue(response.body())
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