package com.autonomad.data.models.claims

import com.autonomad.data.models.claim_user.ServiceImage
import com.google.gson.annotations.SerializedName

data class Specialist(
    val id: Int,
    val description: String?,
    @SerializedName("review_count")
    val reviewCount: Int?,
    @SerializedName("is_full_master")
    val isFullMaster: Boolean?,
    @SerializedName("offers_count")
    val offersCount: Int?,
    @SerializedName("sub_category")
    val subcategory: Subcategory?,
    val profile: Profile?,
    @SerializedName("star_avg")
    val starAvg: Double?,
    @SerializedName("is_fav")
    val isFav: Boolean,
    @SerializedName("address_name")
    val address: String?,
    val latitude: String?,
    val longitude: String?,
    @SerializedName("done_works")
    val doneWorks: Int?,
    @SerializedName("service_offers")
    val offersList: List<ServiceOffer>,
    @SerializedName("service_images")
    val images: List<ServiceImage>,
    val stars: StarsCount,
    val offers: List<CategoryOffer>
)

data class StarsCount(
    @SerializedName("1") val one: Double,
    @SerializedName("2") val two: Double,
    @SerializedName("3") val three: Double,
    @SerializedName("4") val four: Double,
    @SerializedName("5") val five: Double
) {
    val sum: Double
        get() = one + two + three + four + five

    fun getTotal() = getPlural(sum.toInt(), PluralForms("оценка", "оценки", "оценок"), false)
}

data class UserSpecialist(
    val id: Int,
    val profile: Profile?,
    @SerializedName("star_avg") val starAvg: Double?,
    @SerializedName("is_fav") val isFav: Boolean,
    @SerializedName("service_offers") val offersList: List<ServiceOffer>,
    @SerializedName("service_images") val images: List<ServiceImage>,
    val stars: StarsCount,
    val offers: List<CategoryOffer>
)

class MasterComplaintRequest {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("master")
    var masterId: Int? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("is_spam")
    var isSpam: Boolean? = false

    @SerializedName("is_inappropriate_content")
    var isInappropriateContent: Boolean? = false

    @SerializedName("is_fake_information")
    var isFakeInfo: Boolean? = false

}