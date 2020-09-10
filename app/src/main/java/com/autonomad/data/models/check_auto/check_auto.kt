package com.autonomad.data.models.check_auto

import com.autonomad.data.models.main_page_car.GarageDriver
import com.autonomad.data.models.main_page_car.Items
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GarageCar(
    val id: Int,
    @SerializedName("state_number")
    val stateNumber: String,
    val vin: String,
    val srts: String,
    @SerializedName("is_main")
    val isMain: Boolean,
    val title: String?,
    @SerializedName("car_color")
    val color: Items?,
    @SerializedName("car_mark")
    val mark: Items?,
    @SerializedName("car_model")
    val model: Items?,
    @SerializedName("main_driver")
    val mainDriver: Int,
    val drivers: List<GarageDriver>,
    @SerializedName("dtp_status")
    val dtpStatus: DtpStatus,
    @SerializedName("history_status")
    val historyStatus: DtpStatus,
    @SerializedName("checkauto_updated_date")
    val updatedDate: String,
    @SerializedName("checkauto_updated_date_readable")
    val updatedDateReadable: String
) {
    val status: String
        get() {
            val history = if (historyStatus.isPurchased) 1 else {
                if (historyStatus.count == 0) 0 else 2
            }

            val dtp = if (dtpStatus.isPurchased) 1 else {
                if (dtpStatus.count == 0) 0 else 2
            }
            return if (history == 2 || dtp == 2) "Найдены данные" else if (history == 1 || dtp == 1) "данные куплены" else "данные не найдены"
        }

    val isError: Boolean = (status != "данные не найдены" && status != "данные куплены")

    val titleFirst: String
        get() = title ?: markModel

    val markModel: String
        get() = if (mark != null && model != null) "${mark.name} ${model.name}" else ""
}

data class Report(
    @SerializedName("vin")
    @Expose
    val vin: String,
    @SerializedName("regnum")
    @Expose
    val registration_number: String,
    @SerializedName("mark")
    @Expose
    val mark: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("vehicle_year")
    @Expose
    val vehicle_year: Int,
    @SerializedName("dtp_number")
    @Expose
    val dtp_number: Int,
    @SerializedName("history_number")
    @Expose
    val history_number: Int,
    @SerializedName("report_price")
    @Expose
    val report_price: String,
    @SerializedName("data")
    @Expose
    val car_info: CarInfo

)

data class CarInfo(
    @SerializedName("vin_number")
    @Expose
    val vin_number: String,
    @SerializedName("mark")
    @Expose
    val mark: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("engine_volume")
    @Expose
    val engine_volume: String,
    @SerializedName("origin_country")
    @Expose
    val origin_country: String,
    @SerializedName("vehicle_category")
    @Expose
    val vehicle_category: String,
    @SerializedName("color")
    @Expose
    val color: String,
    @SerializedName("engine_power_kwt")
    @Expose
    val engine_power_kwt: String,
    @SerializedName("plated_weight")
    @Expose
    val plated_weight: String,
    @SerializedName("empty_weight")
    @Expose
    val empty_weight: String,
    @SerializedName("seating_count")
    @Expose
    val seating_count: String
)

data class CarLookup(
    val vin: String,
    val mark: String?,
    val model: String?,
    @SerializedName("vehicle_year")
    val year: Int,
    @SerializedName("registration_number")
    val regnum: String
) {
    val markModel: String
        get() = if (mark != null && model != null) "$mark $model" else ""
}

data class CarStateNumber(
    @SerializedName("state_number") val stateNumber: String,
    val srts: String = ""
)

data class CarVin(val vin: String)
