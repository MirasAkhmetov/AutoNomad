package com.autonomad.data.models.claim_user

import com.google.gson.annotations.SerializedName

data class ServiceOffer(
    val id: Int = 0,
    @SerializedName("sub_category")
    val subcategory: Subcategoryyy,
    val price: Int = 0
)

data class CategoryOffer(
    val category: String,
    @SerializedName("sub_categories") val subcategories: List<SubcategoryOffer>
)

data class SubcategoryOffer(@SerializedName("sub_category") val subcategory: String, val price: Int)

data class CreateOffer(
    val id: Int? = null,
    @SerializedName("sub_category")
    val subcategory: Int,
    val price: Int = 0
) {
    companion object {
        fun fromServiceOffer(service: ServiceOffer, withId: Boolean = false) =
            CreateOffer(id = if (withId) service.id else 0, subcategory = service.subcategory.id, price = service.price)
    }
}