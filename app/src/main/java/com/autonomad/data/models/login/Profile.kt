package com.autonomad.data.models.login

import com.autonomad.data.models.Status
import com.google.gson.annotations.SerializedName

data class Profile(
    val id: Int,
    val phone: String,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val patronymic: String,
    val city: City?,
    val avatar: String?,
    @SerializedName("date_of_birth")
    val birthdate: String?,
    val gender: Status,
    @SerializedName("avatar_thumbnail")
    val thumbnail: String?
)
data class Profiletwo(
    val id: Int,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val patronymic: String,
    val city: Int?,
    val avatar: String?,
    val gender: Int,
    @SerializedName("date_of_birth")
    val birthdate: String?
)

data class City(
    val id: Int,
    val name: String,
    val population: Int?,
    val priority: Int?,
    val latitude: String?,
    val longitude: String?,
    val country: Int
)

data class Country(val id: Int, val name: String)

data class CityUpdate(val city: Int)
data class UserUpdate(val email: String?, val firstName: String?, val lastName: String?, val patronymic: String?, val gender: Int?, val date_of_birth: String?)