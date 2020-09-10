package com.autonomad.data.models.penalty

import com.google.gson.annotations.SerializedName

data class PenaltyCheck(
    val id: Int,
    @SerializedName("payment_check")
    val paymentCheck: PaymentCheck,
    val penalty: PenaltyDetail,
    var show: Boolean
)

data class PaymentCheck(
    val id: Int,
    val status: String,
    @SerializedName("payment_date")
    val paymentDate: String,
    @SerializedName("payment_size")
    val paymentSize: String,
    @SerializedName("penalty_count")
    val penaltyCount: Int
)

data class PaymentHistory(
    val id: Int,
    val order: PaymentOrder,
    @SerializedName("act_num") val actNum: String,
    val qualification: String,
    val name: String,
    var show: Boolean
)

data class PaymentOrder(@SerializedName("created_date") val createdDate: String, val paid: String, val pan: String)