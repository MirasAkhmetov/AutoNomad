package com.autonomad.data.models.main_page_car

import com.autonomad.data.models.check_auto.DtpStatus
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cars(
    @SerializedName("count")
    @Expose
    val count: Int,
    @SerializedName("results")
    @Expose
    val result: List<Result>

)

data class Result(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("state_number")
    @Expose
    val state_number: String,
    @SerializedName("vin")
    @Expose
    val vin: String,
    @SerializedName("srts")
    @Expose
    val srts: String,
    @SerializedName("is_main")
    @Expose
    val is_main: Boolean,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("age")
    @Expose
    val age: String,
    @SerializedName("mileage")
    @Expose
    val mileage: String,
    @SerializedName("car_color")
    @Expose
    val car_color: CarColor,
    @SerializedName("car_type")
    @Expose
    val car_type: CarType,
    @SerializedName("car_mark")
    @Expose
    val mark: CarMark,
    @SerializedName("car_model")
    @Expose
    val model: CarModel,
    @SerializedName("car_generation")
    @Expose
    val generation: CarGeneration,
    @SerializedName("car_serie")
    @Expose
    val serie: CarSerie,
    @SerializedName("car_modification")
    @Expose
    val modification: CarModification,
    @SerializedName("car_equipment")
    @Expose
    val car_equipment: String,
    @SerializedName("main_driver")
    @Expose
    val main_driver: String,
    @SerializedName("drivers")
    @Expose
    val drivers: List<GarageDriver>,
    @SerializedName("dtp_status")
    val dtpStatus: DtpStatus,
    @SerializedName("history_status")
    val historyStatus: DtpStatus,
    @SerializedName("checkauto_updated_date")
    val checkAutoUpdatedDate: String
)

data class CarColor(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String
)
data class CarType(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("ext")
    @Expose
    val ext: String,
    @SerializedName("name")
    @Expose
    val name: String
)
data class CarMark(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("ext")
    @Expose
    val ext: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("name_ru")
    @Expose
    val name_ru: String,
    @SerializedName("car_type_ext")
    @Expose
    val car_type_ext: String,
    @SerializedName("car_type")
    @Expose
    val car_type: String
)
data class CarModel(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("ext")
    @Expose
    val ext: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("name_ru")
    @Expose
    val name_ru: String,
    @SerializedName("car_mark_ext")
    @Expose
    val car_mark_ext: String,
    @SerializedName("car_mark")
    @Expose
    val car_mark: String
)
data class CarGeneration(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("ext")
    @Expose
    val ext: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("year_begin")
    @Expose
    val year_begin: String,
    @SerializedName("year_end")
    @Expose
    val year_end: String,
    @SerializedName("car_model_ext")
    @Expose
    val car_model_ext: String,
    @SerializedName("car_model")
    @Expose
    val car_model: String
)
data class CarSerie(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("ext")
    @Expose
    val ext: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("car_generation_ext")
    @Expose
    val car_generation_ext: String,
    @SerializedName("car_model_ext")
    @Expose
    val car_model_ext: String,
    @SerializedName("car_generation")
    @Expose
    val car_generation: String,
    @SerializedName("car_model")
    @Expose
    val car_model: String
)
data class CarModification(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("ext")
    @Expose
    val ext: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("car_serie_ext")
    @Expose
    val car_serie_ext: String,
    @SerializedName("car_serie")
    @Expose
    val car_serie: String
)
