package com.autonomad.data.models.check_auto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateOrder(
    @SerializedName("vin_number")
    @Expose
    val vinNumber: String,
    @SerializedName("include_dtp")
    @Expose
    val includeDtp: Boolean,
    @SerializedName("include_history")
    @Expose
    val includeHistory: Boolean
)

data class CreateOrderStatus(
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_id")
    var orderId: String,
    @SerializedName("price")
    var price: String
)