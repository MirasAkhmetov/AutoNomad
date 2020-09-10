package com.autonomad.ui.main_page.tech_inspection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.check_auto.CarStateNumber
import com.autonomad.data.models.main_page_car.CarId
import com.autonomad.data.models.main_page_car.Inspection
import com.autonomad.utils.*
import org.json.JSONObject

class TechInspectionViewModel(private val carId: Int, private val stateNumber: String, private val srts: String) :
    DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val isCheck = MutableLiveData<Status<Boolean>>()
    val check: LiveData<Status<Boolean>> = isCheck

    val liveStateNumber = MutableLiveData<String>()
    val liveTitle = MutableLiveData<String>()

    private val mInspection = MutableLiveData<Status<Inspection>>()
    val inspection: LiveData<Status<Inspection>> by lazy {
        loadData()
        mInspection
    }

    private val mSaveResult = MutableLiveData<Status<Boolean>>()
    val saveResult: LiveData<Status<Boolean>> = mSaveResult

    private fun loadData() {
        mInspection.value = Status.loading
        val check = if (carId < 1) {
            if (stateNumber.isEmpty()) "not set".fail else true.success
        } else false.success
        isCheck.value = check
        (if (check?.item == true) {
            liveStateNumber.value = stateNumber
            apiService.checkInspection(CarStateNumber(stateNumber, srts))
        } else apiService.getInspection(CarId(carId)))
            .subscribeAndDispose({
                val errorBody = it.errorBody()?.string()
                mInspection.value = if (errorBody?.contains(TI_NOT_NEEDED) == true) {
                    try {
                        val jsonObject = JSONObject(errorBody).getJSONObject("car")
                        liveTitle.value = jsonObject.getString("title")
                        if (liveStateNumber.value == null)
                            liveStateNumber.value = jsonObject.getString("state_number")
                    } catch (e: Exception) {
                        timberE(e, "stateNumberFailure")
                    }
                    Status.idle
                } else it.toStatus()
            }, mInspection::fromThrowable)
    }

    fun saveInspection() {
        mSaveResult.value = Status.loading
        apiService.setCar(CarStateNumber(stateNumber, srts)).subscribeAndDispose({
            mSaveResult.value = if (it.isSuccessful) {
                isCheck.value = false.success
                true.success
            } else it.message().fail
        }, mSaveResult::fromThrowable)
    }

    companion object {
        const val TI_NOT_NEEDED = "Машины с возрастом до 7 лет не нуждаются в прохождении ТехОсмотра"
    }
}

@Suppress("UNCHECKED_CAST")
class TechInspectionFactory(private val carId: Int, private val stateNumber: String, private val srts: String) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = TechInspectionViewModel(carId, stateNumber, srts) as T
}