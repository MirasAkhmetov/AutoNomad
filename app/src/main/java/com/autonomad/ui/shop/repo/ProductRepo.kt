package com.autonomad.ui.shop.repo

import com.autonomad.data.models.Page
import com.autonomad.ui.shop.model.NetResult
import com.autonomad.ui.shop.model.PopularCategory
import com.autonomad.ui.shop.model.Product

interface ProductRepo {
    suspend fun getPopularCategories(): List<PopularCategory>
    suspend fun getProducts(): NetResult<Page<Product>>
}