package com.autonomad.data.models.claim_user

import com.google.gson.annotations.SerializedName

data class Categoryyyy(
    val id: Int,
    val name: String,
    val description: String?,
    val image: String?,
    @SerializedName("masters_count")
    val mastersCount: Int = 0,
    @SerializedName("requests_count")
    val requestsCount: Int = 0,
    @SerializedName("sub_categories")
    val subcategories: List<Subcategoryyy>?
)

data class Subcategoryyy(val id: Int, val name: String, val category: Categoryyyy)