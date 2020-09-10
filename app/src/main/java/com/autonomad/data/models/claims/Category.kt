package com.autonomad.data.models.claims

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Int,
    val name: String,
    val description: String?,
    val image: String?,
    @SerializedName("masters_count")
    val mastersCount: Int?,
    @SerializedName("requests_count")
    val requestsCount: Int?,
    @SerializedName("sub_categories")
    val subcategories: List<Subcategory>?
)

data class Subcategory(val id: Int, val name: String, val category: Category)

data class Categorytwo(
    val id: Int,
    val name: String,
    val image: String?,
    @SerializedName("sub_category")
    val subcategories: List<Subcategorytwo>?,
    val price: Int
)

data class Subcategorytwo(val id: Int, val name: String, val category: Categorytwo)