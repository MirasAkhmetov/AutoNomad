package com.autonomad.ui.insurance.insurance_home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.ui.insurance.insurance_list.SerializableLiveData
import com.autonomad.ui.insurance.repo.InsuranceRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class InsuranceHomeViewModel(private val repo: InsuranceRepo) : ViewModel() {
    var insurancesFavourite = SerializableLiveData<List<InsuranceHistory>>()
    var insurancesHistory = SerializableLiveData<List<InsuranceHistory>>()
    var loading = MutableLiveData<Boolean>()
    var showDriverDeletedDialog = MutableLiveData<Boolean>()
    var errorMsg = MutableLiveData<String>()

    fun update() {
        getFavourites()
        getHistory()
    }

    fun getFavourites() {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getMyFavourites()
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            insurancesFavourite.postValue(response.body()?.list)
                        }
                        404 -> {
                        }
                        500 -> {
                        }
                        else -> {
                        }
                    }
                } catch (e: Exception) {
                    errorMsg.postValue("Нет интернет соединения")
                }
            }
        }
    }

    fun getHistory() {
        loading.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repo.getMyHistory()
                    loading.postValue(false)
                    when (response.code()) {
                        200, 201 -> {
                            Timber.d(response.toString())
                            insurancesHistory.postValue(response.body()?.list)
                        }
                        404 -> {
                        }
                        500 -> {
                        }
                        else -> {
                        }
                    }
                } catch (e: Exception) {
                    errorMsg.postValue("Нет интернет соединения")
                }
            }
        }
    }
}