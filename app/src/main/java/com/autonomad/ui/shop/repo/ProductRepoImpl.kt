package com.autonomad.ui.shop.repo

import com.autonomad.data.models.Page
import com.autonomad.network.ApiShop
import com.autonomad.ui.shop.model.NetResult
import com.autonomad.ui.shop.model.PopularCategory
import com.autonomad.ui.shop.model.Product
import com.autonomad.ui.shop.model.getResult

class ProductRepoImpl(
    private val apiShop: ApiShop
) : ProductRepo {

    override suspend fun getPopularCategories(): List<PopularCategory> {
        val list = arrayListOf<PopularCategory>()//todo
        list.add(PopularCategory(1, "Транспорт", 2))
        list.add(PopularCategory(2, "Комерческий транспорт", 2))
        list.add(PopularCategory(5, "Двигатели", 2))
        list.add(PopularCategory(3, "Мотоциклы", 2))
        return list
    }

    override suspend fun getProducts(): NetResult<Page<Product>> {
        return try {
            apiShop.getProducts().getResult()
        } catch (e: Exception) {
            e.printStackTrace()
            NetResult.Error("Error ${e.localizedMessage}")
        }
    }

}