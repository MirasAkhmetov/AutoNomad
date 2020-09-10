package com.autonomad.data.models.main_page_car

import com.autonomad.data.models.check_auto.GarageCar
import com.google.gson.annotations.SerializedName

data class Inspection(
    val id: Int,
    @SerializedName("car") val carId: Int,
    @SerializedName("inspection_date") val inspectionDate: String,
    @SerializedName("expiration_date") val expirationDate: String,
    @SerializedName("car_info") val carInfo: GarageCar
)