package com.autonomad.ui.claims.specialist_claims.settings.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.claims.SpecialistStatistics
import com.autonomad.utils.*

class StatisticsViewModel : DisposableViewModel() {
    private val apiService = ApiRequestsFactory.apiService

    private val mStatistics = MutableLiveData<Status<SpecialistStatistics>>()
    val statistics: LiveData<Status<SpecialistStatistics>> by lazy {
        loadData()
        mStatistics
    }

    private fun loadData() {
        apiService.analytics().subscribeAndDispose(mStatistics::fromResponse, mStatistics::fromThrowable)
    }
}