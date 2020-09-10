package com.autonomad.data.models.check_auto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckAutoHistory(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("updated_at")
    @Expose
    var date: String,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("tranport_data")
    @Expose
    val vehicle: CarInfo?,
    var show: Boolean
) {

    val title: String
        get() = if (vehicle != null) "${vehicle.mark} ${vehicle.model}" else "Не найдено информация об авто"
}

data class DetailTicket(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("order_id")
    @Expose
    val order_id: String,
    @SerializedName("updated_at")
    @Expose
    val updated_at: String,
    @SerializedName("user")
    @Expose
    val user: User,
    @SerializedName("report")
    @Expose
    val report: CarReport,
    @SerializedName("include_dtp")
    @Expose
    val include_dtp: Boolean,
    @SerializedName("include_history")
    @Expose
    val include_history: Boolean,
    @SerializedName("card")
    @Expose
    val card: Card,

    var date1: String,
    var date2: String


)

data class Card(
    @SerializedName("last_four")
    @Expose
    val last_four: String?,
    @SerializedName("type")
    @Expose
    val type: String?,
    @SerializedName("error_message")
    @Expose
    val error_message: String?

)

data class User(
    @SerializedName("first_name")
    @Expose
    val first_name: String,
    @SerializedName("last_name")
    @Expose
    val last_name: String,
    @SerializedName("patronymic")
    @Expose
    val patronymic: String

)

data class CarReport(
    @SerializedName("car_histories")
    @Expose
    val car_histories: List<CarHistories>,
    @SerializedName("car_dtps")
    @Expose
    val car_dtps: List<CarDtps>,
    @SerializedName("data")
    @Expose
    val car_info: CarInfo
)

data class CarHistories(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("person_type")
    @Expose
    val person_type: String,
    @SerializedName("action_date")
    @Expose
    val action_date: String,
    @SerializedName("action")
    @Expose
    val action: String

)

data class CarDtps(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("dtp_date")
    @Expose
    val dtp_date: String,
    @SerializedName("description")
    @Expose
    val description: String
)

data class DtpStatus(
    @SerializedName("order_id")
    val id: Int,
    val count: Int,
    @SerializedName("is_already_purchased")
    val isPurchased: Boolean
) {
    val state: Int
        get() = when {
            isPurchased -> 2
            count == 0 -> 0
            count > 0  -> 1
            else -> 0
        }
}