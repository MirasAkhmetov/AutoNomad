package com.autonomad.data.models.main_page_car

import com.autonomad.data.models.check_auto.DtpStatus
import com.google.gson.annotations.SerializedName

data class CarDetails(
    val id: Int,
    @SerializedName("state_number")
    val stateNumber: String,
    val vin: String,
    val srts: String,
    @SerializedName("is_main")
    val isMain: Boolean,
    val age: String,
    val title: String,
    val mileage: String,
    @SerializedName("car_equipment")
    val equipment: Int,
    @SerializedName("main_driver")
    val mainDriver: Int,
    val drivers: List<GarageDriver>,
    @SerializedName("car_color")
    val color: Items,
    @SerializedName("car_mark")
    val mark: Items,
    @SerializedName("car_model")
    val model: Items,
    @SerializedName("car_generation")
    val generation: Items,
    @SerializedName("car_serie")
    val serie: Items,
    @SerializedName("car_modification")
    val modification: Items,
    @SerializedName("dtp_status")
    val dtpStatus: DtpStatus,
    @SerializedName("history_status")
    val historyStatus: DtpStatus,
    @SerializedName("check_auto_updated_date")
    val checkAutoUpdatedDate: String
)

data class GarageDriver(
    val id: Int,
    val name: String,
    val target: String,
    val target_type: Int
)