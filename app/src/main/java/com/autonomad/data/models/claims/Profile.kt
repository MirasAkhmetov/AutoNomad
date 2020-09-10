package com.autonomad.data.models.claims

import com.autonomad.data.models.Status
import com.google.gson.annotations.SerializedName

data class Profile (
    val phone: String,
    val email: String,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    val patronymic: String?,
    val gender: Status,
    val avatar: String,
    @SerializedName("avatar_thumbnail")
    val thumbnail: String,
    val city: Int?
)
