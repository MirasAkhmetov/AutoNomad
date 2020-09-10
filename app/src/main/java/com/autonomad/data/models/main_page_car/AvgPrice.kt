package com.autonomad.data.models.main_page_car

import com.google.gson.annotations.SerializedName

data class AvgPrice(val vin: String, @SerializedName("average_price") val price: String?)