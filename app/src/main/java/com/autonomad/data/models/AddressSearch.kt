package com.autonomad.data.models

import com.autonomad.data.models.login.City
import com.autonomad.data.models.main_page_car.Items
import com.google.gson.annotations.SerializedName

data class AddressSearch(val id: Int, val name: String, val longitude: String, val latitude: String)

data class Address(
    val id: Int,
    val user: Int,
    val latitude: String,
    val longitude: String,
    val description: String?,
    val country: Items,
    val city: City,
    @SerializedName("extra_info")
    val extra: String,
    val type: String
) {
    val addressType: AddressType
        get() = when (type) {
            AddressType.Home.string -> AddressType.Home
            AddressType.Work.string -> AddressType.Work
            AddressType.School.string -> AddressType.School
            else -> AddressType.FrequentlyUsed
        }
}

data class AddressCreate(
    val latitude: String,
    val longitude: String,
    val description: String?,
    val country: Int,
    val city: Int,
    @SerializedName("extra_info")
    val extra: String,
    val type: String
)

enum class AddressType(val string: String, val title: String?) {
    Home("HOME", "Дом"),
    Work("WORK", "Работа"),
    School("SCHOOL", "Школа"),
    FrequentlyUsed("FREQUENTLY_USED", null)
}

//infix fun Address.getDistanceTo(other: Address): Double {
//    val (x1, y1) = this.longitude.toDoubleOrNull() to this.latitude.toDoubleOrNull()
//    val (x2, y2) = other.longitude.toDoubleOrNull() to other.latitude.toDoubleOrNull()
//    if (x1 == null || y1 == null || x2 == null || y2 == null)
//        return Double.MAX_VALUE
//}