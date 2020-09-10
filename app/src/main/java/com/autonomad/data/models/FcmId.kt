package com.autonomad.data.models

import com.google.gson.annotations.SerializedName

data class FcmId(
    @SerializedName("registration_id")
    val fcmId: String,
    @SerializedName("is_active")
    val isActive: Boolean = true,
    val type: String = "android"
)