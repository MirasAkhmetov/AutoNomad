package com.autonomad.ui.penalty.penalties_of_person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.penalty.PenaltySearch
import com.autonomad.data.repo.PenaltiesRepo
import com.autonomad.network.ApiGarage
import com.autonomad.utils.DisposableViewModel
import com.autonomad.utils.Status
import com.autonomad.utils.fail
import com.autonomad.utils.success
import kotlinx.coroutines.launch

class PenaltiesOfPersonViewModel(
    private val uin: String,
    private val repo: PenaltiesRepo
) : DisposableViewModel() {

    private val mResult = MutableLiveData<Status<PenaltySearch>>()
    val result: LiveData<Status<PenaltySearch>> by lazy {
        penaltySearch()
        mResult
    }

    private val mDriverSaved = MutableLiveData(true.success!!)
    val driverSaved: LiveData<Status<Boolean>> = mDriverSaved

    private fun penaltySearch() {
        mResult.value = Status.loading
        viewModelScope.launch {
            mResult.value = repo.searchPenaltiesByUin(uin)
        }
    }

    fun saveDriver(save: Boolean) {
        val id = mResult.value?.item?.driver?.id
        if (id == null) {
            mDriverSaved.value = "Не удалось найти пользователя".fail
            return
        }
        mDriverSaved.value = Status.loading
        viewModelScope.launch {
            val response = repo.saveDriver(id, save)
            mDriverSaved.value = when {
                response.isSuccessful -> save.success
                response.code() == 404 -> "Пользователь не найден".fail
                else -> "Не удалось сохранить изменения".fail
            }
        }
    }
}
