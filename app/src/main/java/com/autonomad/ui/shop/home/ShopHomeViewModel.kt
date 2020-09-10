package com.autonomad.ui.shop.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.autonomad.ui.shop.model.NetResult
import com.autonomad.ui.shop.model.PopularCategory
import com.autonomad.ui.shop.model.Product
import com.autonomad.ui.shop.repo.ProductRepo
import com.autonomad.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopHomeViewModel(
    private val repo: ProductRepo
) : BaseViewModel() {

    val toast = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val popularCategory = MutableLiveData<List<PopularCategory>>()
    val products = MutableLiveData<List<Product>>()


    fun getPopularCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            popularCategory.postValue(repo.getPopularCategories())
        }
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            val response = repo.getProducts()
            loading.postValue(false)
            when (response) {
                is NetResult.Success -> products.postValue(response.data.list)
                is NetResult.Error -> toast.postValue(response.errorMessage)
            }
        }
    }


}