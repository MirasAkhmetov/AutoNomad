package com.autonomad.data.models.claim_user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UplooodImage(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("thumbnail")
    @Expose
    val thumbnail: String
)
data class Servicesreq(
    @SerializedName("count")
    @Expose
    val count: Int,
    @SerializedName("next")
    @Expose
    val next: String,
    @SerializedName("previous")
    @Expose
    val previous: String,
    @SerializedName("results")
    @Expose
    val results: List<Services>
)
data class Services(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("budget")
    @Expose
    val budget: Int,
    @SerializedName("created")
    @Expose
    val created: String,
    @SerializedName("created_readable")
    @Expose
    val createdReadable: String,
    @SerializedName("status")
    @Expose
    val status: StatusType,
    @SerializedName("response_count")
    @Expose
    val response_count: Int
)
data class StatusType(
    @SerializedName("value")
    @Expose
    val value: Int,
    @SerializedName("display")
    @Expose
    val display: String
)

data class ServiceAdreska(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("longitude")
    @Expose
    val longitude: String,
    @SerializedName("latitude")
    @Expose
    val latitude: String
)
class ServiceCreate {
    @SerializedName("sub_categories")
    @Expose
    var subCategories: List<Int>? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("budget")
    @Expose
    var budget: Long? = null

    @SerializedName("address")
    @Expose
    var address: ServiceAdreska? = null

    @SerializedName("start_day")
    @Expose
    var startDay: String? = null

    @SerializedName("end_day")
    @Expose
    var endDay: String? = null

    @SerializedName("negotiable_price")
    @Expose
    var negotiablePrice: Boolean? = null

    @SerializedName("images")
    @Expose
    var images: List<Int>? = null

    @SerializedName("car")
    @Expose
    var car: Int? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: Int? = null
}
