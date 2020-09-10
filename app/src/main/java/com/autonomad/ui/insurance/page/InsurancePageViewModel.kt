package com.autonomad.ui.insurance.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.data.models.insurance.NewCheck
import com.autonomad.utils.*

class InsurancePageViewModel(private val insuranceId: Int) : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mInsurance = MutableLiveData<Status<InsuranceHistory>>()
    val insurance: LiveData<Status<InsuranceHistory>> by lazy {
        loadData()
        mInsurance
    }

    private fun loadData() {
        apiService.getInsurance(insuranceId).subscribeOnIo()
            .subscribe({
                mInsurance.value = it.toStatus()
            }, {
                mInsurance.value = it.localizedMessage.fail
            }).disposeOnCleared()
    }

    private val mSave = MutableLiveData<Status<Boolean>>()
    val save: LiveData<Status<Boolean>> = mSave

    fun save(save: Boolean) {
        val uin = mInsurance.value?.item?.driver?.target
        val stateNumber = mInsurance.value?.item?.cars?.stateNumber
        if (uin == null || stateNumber == null) {
            mSave.value = "Страховка не найдена".fail
            return
        }
        mSave.value = Status.loading
        apiService.checkInsurance(NewCheck(uin, stateNumber, save)).subscribeAndDispose({
            if (it.isSuccessful) mSave.value = (it.body()?.isFavorite ?: save).success
            else {
                mSave.value = it.message().fail
                mSave.value = (!save).success
            }
        }, {
            mSave.value = it.localizedMessage.fail
        })
    }
}

@Suppress("UNCHECKED_CAST")
class InsurancePageViewModelFactory(private val id: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = InsurancePageViewModel(id) as T
}