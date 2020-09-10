package com.autonomad.data.models.penalty

import com.google.gson.annotations.SerializedName

data class DriverCheck(
    @SerializedName("Error") val error: String,
    @SerializedName("target") val uin: String,
    @SerializedName("full_name") val fullName: String
)

data class DriverCheckRequest(@SerializedName("target") val uin: String)

data class DriverCreate(
    val id: Int,
    val profile: Int,
    val name: String,
    @SerializedName("is_main") val isMain: Boolean,
    val target: String
)