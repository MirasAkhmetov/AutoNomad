package com.autonomad.data.models.claims

import com.autonomad.data.models.AddressSearch
import com.autonomad.data.models.Status
import com.autonomad.data.models.claim_user.ServiceAdreska
import com.autonomad.data.models.claim_user.ServiceImage
import com.autonomad.data.models.main_page_car.MyClaimCar
import com.autonomad.utils.months
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ServiceRequest(
    val id: Int,
    @SerializedName("sub_categories")
    val subcategories: List<Subcategory>,
    val title: String,
    val description: String,
    val budget: Int?,
    val address: AddressSearch,
    @SerializedName("start_day")
    val startDay: String?,
    @SerializedName("end_day")
    val endDay: String?,
    val status: Status,
    val created: String?,
    @SerializedName("created_readable")
    val createdReadable: String?,
    @SerializedName("negotiable_price")
    val negotiablePrice: Boolean = false,
    val master: String,
    @SerializedName("response_count")
    val responseCount: Int,
    val profile: Profile,
    val images: List<ServiceImage>,
    val car: MyClaimCar?=null
) {
    private fun getDate(date: String?): String {
        if (date == null) return ""
        val (year, month, day) = date.split("-").map {
            it.toIntOrNull() ?: return ""
        }
        return "$day ${months[month - 1]}${if (year != Calendar.getInstance().get(Calendar.YEAR)) " $year" else ""}"
    }

    val time: String
        get() {
            return if (startDay == null && endDay == null) ""
            else if (startDay == null) "до ${getDate(endDay)}"
            else if (endDay == null) "с ${getDate(startDay)}"
            else "${getDate(startDay)} - ${getDate(endDay)}"
        }
}

class ServiceModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("sub_categories")
    @Expose
    var subCategories: List<SubCategoryModel>? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("budget")
    @Expose
    var budget: Int? = null

    @SerializedName("address")
    @Expose
    var address: ServiceAdreska? = null

    @SerializedName("start_day")
    @Expose
    var startDay: String? = null

    @SerializedName("end_day")
    @Expose
    var endDay: String? = null

    @SerializedName("created_readable")
    @Expose
    var createdReadable: String? = null

    @SerializedName("response_count")
    @Expose
    var responseCount: Int? = null

    @SerializedName("status")
    @Expose
    var status: Status? = null

    @SerializedName("negotiable_price")
    @Expose
    var negotiablePrice: Boolean = false

    @SerializedName("images")
    @Expose
    var images: List<ServiceImage>? = null

    @SerializedName("car")
    var car: CarModel? = null
}

class SubCategoryModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("category")
    @Expose
    var category: CategoryModel? = null
}

class CategoryModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null
}

class CarModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("state_number")
    @Expose
    var stateNumber: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("detail")
    @Expose
    var detail: String? = null

}