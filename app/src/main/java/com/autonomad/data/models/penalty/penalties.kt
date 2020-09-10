package com.autonomad.data.models.penalty

import com.autonomad.data.models.Status
import com.autonomad.data.models.insurance.InsuranceCar
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Driver(
    val id: Int,
    val profile: Int,
    val name: String,
    @SerializedName("is_main")
    val isMain: Boolean,
    val target: String,
    @SerializedName("target_type")
    val targetType: Status,
    val cars: List<InsuranceCar>,
    @SerializedName("main_car")
    val mainCar: String,
    @SerializedName("last_checked")
    val lastChecked: String,
    @SerializedName("last_checked_readable")
    val lastCheckedReadable: String,
    @SerializedName("penalty_count")
    val penaltyCount: Int
)

data class PenaltySearch(
    val driver: PenaltyDriver,
    val result: List<DateResult>?,
    val error: Boolean?,
    val message: String?,
    @SerializedName("penalty_count") val count: Int?
)

data class DateResult(val date: String, @SerializedName("penalties_count") val count: Int, val penalties: List<Result>)

data class Result(
    val id: Int,
    @SerializedName("av_original_id")
    val avOriginalId: String,
    @SerializedName("commission_date")
    val commissionDate: String,
    @SerializedName("offence_org")
    val offenceOrg: String,
    val qualification: String,
    @SerializedName("balance_size")
    val balanceSize: String
)

data class PenaltyDriver(
    val id: Int,
    val profile: Int,
    val name: String,
    val target: String,
    @SerializedName("target_type") val targetType: Int,
    @SerializedName("is_main") val isMain: Boolean
)

data class SaveRequest(@SerializedName("is_saved") val isSaved: Boolean)

data class PenaltyDetail(
    val id: Int,
    @SerializedName("target_type")
    val targetType: Status,
    val status: Status,
    val target: String,
    @SerializedName("act_num")
    val actNum: String,
    @SerializedName("av_original_id")
    val av_original_id: String,
    val birthday: String?,
    @SerializedName("commission_date")
    val commissionDate: String?,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val patronymic: String,
    @SerializedName("org_name")
    val orgName: String?,
    val kbk: String,
    val kno: String,
    val knp: String,
    @SerializedName("offence_org")
    val offenceOrg: String,
    @SerializedName("protocol_date")
    val protocolDate: String?,
    @SerializedName("protocol_num")
    val protocolNum: String,
    val qualification: String,
    @SerializedName("penalty_size")
    val penaltySize: Double,
    @SerializedName("payment_size")
    val paymentSize: Double,
    @SerializedName("balance_size")
    val balance_size: Double,
    @SerializedName("create_date")
    val createDate: String,
    val driver: Int?,
    val order: Int?
)

data class PenaltyHistoryDetail(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("driver")
    @Expose
    val driver: Driver,
    @SerializedName("order")
    @Expose
    val order: Order,
    @SerializedName("av_original_id")
    @Expose
    val av_original_id: String,
    @SerializedName("commission_date")
    @Expose
    var commission_date: String,
    @SerializedName("qualification")
    @Expose
    val qualification: String,
    var show: Boolean
)

data class Order(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("order_id")
    @Expose
    val order_id: String,
    @SerializedName("base_amount")
    @Expose
    val base_amount: String
)

data class PenaltyId(
    @SerializedName("penalty")
    @Expose
    val penaltyId: Int
)

data class MakeOrder(
    val id: Int,
    val penalties: List<Int>,
    val status: Int,
    @SerializedName("order_ext")
    val orderExt: String,
    @SerializedName("base_amount")
    val baseAmount: Double,
    @SerializedName("bank_commission")
    val bankCommission: Double,
    @SerializedName("our_commission")
    val ourCommission: Double,
    @SerializedName("user_ip")
    val userIp: String,
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("form_url")
    val formUrl: String
)

data class OrderDetail(
    val id: Int,
    val penalties: List<Result>,
    val status: Status,
    @SerializedName("order_ext")
    val orderExt: String,
    @SerializedName("base_amount")
    val baseAmount: Double,
    @SerializedName("bank_commission")
    val bankCommission: Double,
    @SerializedName("our_commission")
    val ourCommission: Double,
    @SerializedName("user_ip")
    val userIp: String,
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("form_url")
    val formUrl: String
)