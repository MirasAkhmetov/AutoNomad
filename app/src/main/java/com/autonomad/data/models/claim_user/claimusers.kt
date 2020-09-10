package com.autonomad.data.models.claim_user

import com.autonomad.utils.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class createInfo(
    val carNumber: String,
    val title: String
)

data class Categories(
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
    val results: List<Category>
)

data class Category(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("masters_count")
    @Expose
    val masters_count: Int
)

data class CategoriesId(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("sub_categories")
    @Expose
    val results: List<SubCategory>
)

data class SpiritualTeacher(var name: String?, val quote: String) {
    var isSelected: Boolean = false
}
data class SubCategory(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("masters_count")
    val mastersCount: Int,
    @SerializedName("isfall")
    @Expose
    var isfall: Boolean = false
)

data class Masters(
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
    val results: List<Master>
)

data class MastersList(
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
    val results: List<ClaimMasterResult>
)

data class ClaimMasterResult (
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("master")
    @Expose
    val master: Master
)

data class Master(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("review_count")
    @Expose
    val review_count: Int,
    @SerializedName("star_avg")
    @Expose
    val star_avg: Double,
    @SerializedName("is_fav")
    @Expose
    val is_fav: Boolean,
    @SerializedName("profile")
    @Expose
    val prof: Profile?
)

data class Profile(
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("first_name")
    @Expose
    val first_name: String,
    @SerializedName("last_name")
    @Expose
    val last_name: String,
    @SerializedName("patronymic")
    @Expose
    val patronymic: String,
    @SerializedName("date_of_birth")
    @Expose
    val dateOfBirth: String,
    @SerializedName("avatar")
    @Expose
    val avatar: String?,
    @SerializedName("avatar_thumbnail")
    @Expose
    val avatar_thumbnail: String
)

data class MastersId(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("profile")
    @Expose
    val profile: Profile,
    @SerializedName("review_count")
    @Expose
    val review_count: Int,
    @SerializedName("star_avg")
    @Expose
    val star_avg: Double,
    @SerializedName("is_fav")
    @Expose
    val is_fav: Boolean,
    @SerializedName("service_offers")
    @Expose
    val service_offers: List<Serviceoffers>,
    @SerializedName("service_images")
    @Expose
    val service_images: List<ServiceImage>,
    @SerializedName("stars")
    @Expose
    val stars: Stars,
    @SerializedName("address_name")
    @Expose
    val address_name: String,
    @SerializedName("offers")
    @Expose
    val offers: List<Offers>
)
data class Offers(
    @SerializedName("category")
    @Expose
    val category: String,
    @SerializedName("sub_categories")
    @Expose
    val sub_categories: List<SubCategoryOffers>
)
data class SubCategoryOffers(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("sub_category")
    @Expose
    val sub_category: String
)

data class Serviceoffers(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("sub_category")
    @Expose
    val sub_category: SubCategoryTwo,
    @SerializedName("price")
    @Expose
    val price: Int
)

data class SubCategoryTwo(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("category")
    @Expose
    val category: CategoryTwo
)

data class CategoryTwo(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("image")
    @Expose
    val image: String
)

data class ServiceImage(
    val id: Int,
    val image: String,
    val thumbnail: String?
) {
    val thumbnailFull: String
        get() = if (thumbnail != null) "${Constants.BASE_URL_AUTO_REQ.dropLast(1)}$thumbnail" else image
}

data class Stars(
    @SerializedName("one")
    @Expose
    val one: Int? = 0,
    @SerializedName("two")
    @Expose
    val two: Int? = 0,
    @SerializedName("three")
    @Expose
    val three: Int? = 0,
    @SerializedName("four")
    @Expose
    val four: Int? = 0,
    @SerializedName("five")
    @Expose
    val five: Int? = 0
)

data class MastersReview(
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
    val resultss: List<Review>
)

data class Review(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("text")
    @Expose
    val text: String,
    @SerializedName("star")
    @Expose
    val star: Int,
    @SerializedName("profile")
    @Expose
    val profile: Profile,
    @SerializedName("created")
    @Expose
    val created: String
)

data class MastID(
    @SerializedName("master_id")
    @Expose
    var master_id: String
)
