package com.autonomad.ui.claims.specialist_claims.home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.autonomad.data.models.claims.Category
import com.autonomad.utils.*

class CategoryListViewModel(private val categoryId: Int) : DisposableViewModel() {
    private val repository = ApiRequestsFactory.apiService

    private val mStatus = MutableLiveData<Status<Category>>()
    val status: LiveData<Status<Category>> by lazy {
        getData()
        mStatus
    }

    private fun getData() {
        mStatus.value = Status.loading
        repository.getCategoryById(categoryId).subscribeAndDispose(mStatus::fromResponse, mStatus::fromThrowable)
    }
}

@Suppress("UNCHECKED_CAST")
class CategoryListViewModelProvider(private val categoryId: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = CategoryListViewModel(categoryId) as T
}