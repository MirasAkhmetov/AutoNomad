package com.autonomad.ui.shop.model

data class Product(
    val title: String?,
    val article: String?,
    val supplier: Supplier,
    val cash_back: Int?,
    val discount: Int?,
    val price: String?,
    val id: Long?,
    val created_at: String?,
    val updated_at: String?,
    val avg_star: String?
)

data class Supplier(
    val id: Long?,
    val description: String?
)