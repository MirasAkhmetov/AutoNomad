package com.autonomad.network

import com.autonomad.data.models.Page
import com.autonomad.ui.shop.model.Product
import com.autonomad.ui.shop.model.ShopCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiShop {

    @GET("api/products/")
    suspend fun getProducts(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): Response<Page<Product>>


    @GET("api/categories/list/")
    suspend fun getCategories(
        @Query("transport_pk") transport_pk: Int? = null,
        @Query("treeparent_pk") treeparent_pk: Int? = null,
        @Query("tree_subnode") tree_subnode: Int? = null
    ): Response<List<ShopCategory>>
}