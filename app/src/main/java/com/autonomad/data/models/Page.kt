package com.autonomad.data.models

import androidx.lifecycle.MutableLiveData
import com.autonomad.utils.Status
import com.autonomad.utils.success
import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class Page<T>(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    @SerializedName("results")
    val list: List<T> = emptyList()
)

fun <T> MutableLiveData<Status<Page<T>>>.addPage(currentPage: Int, response: Response<Page<T>>) =
    if (response.isSuccessful) {
        if (currentPage == 0) postValue(response.body().success)
        else {
            val item = response.body() ?: Page()
            postValue(item.copy(list = item.list + (value?.item?.list ?: emptyList())).success)
        }
        true
    } else {
        Status.setFailed(this, response.message())
        false
    }
