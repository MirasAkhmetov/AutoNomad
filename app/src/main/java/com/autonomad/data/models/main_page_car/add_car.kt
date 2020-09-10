package com.autonomad.data.models.main_page_car

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetItems(
    @SerializedName("count")
    @Expose
    val count: Int,
    @SerializedName("results")
    @Expose
    val result: List<Items>
)

data class Items(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String
)


data class Car(
    var mark_id: Int,
    var mark: String,
    var model_id: Int,
    var model: String,
    var color_id: Int,
    var color: String,
    var generation_id: Int,
    var generation: String,
    var series_id: Int,
    var series: String,
    var modification_id: Int,
    var modification: String,
    var state: Boolean
)

data class addCar(
    @SerializedName("state_number")
    @Expose
    val state_number: String,
    @SerializedName("mileage")
    @Expose
    val mileage: Int,
    @SerializedName("age")
    @Expose
    val age: Int,
    @SerializedName("car_color")
    @Expose
    val car_color: Int,
    @SerializedName("car_model")
    @Expose
    val car_model: Int,
    @SerializedName("car_serie")
    @Expose
    val car_serie: Int,
    @SerializedName("car_generation")
    @Expose
    val car_generation: Int,
    @SerializedName("car_mark")
    @Expose
    val car_mark: Int,
    @SerializedName("car_modification")
    @Expose
    val car_modification: Int
)

class MyClaimCar {
    @SerializedName("title")
    @Expose
    val title: String? = ""
}

data class SrtsUpdate(val srts: String)
data class IsMainUpdate(@SerializedName("is_main")  @Expose var isMain: Boolean)

data class IsActivUpdate(@SerializedName("status")  @Expose var status_int: Int)
//data class IsMainUpdate(
//    @SerializedName("is_main")
//    @Expose
//    var is_main: Boolean
//)

data class CarUpdated(
    val srts: String,
    val title: String,
    @SerializedName("main_driver")
    val mainDriver: Int,
    val drivers: List<Int>,
    val mileage: Int,
    @SerializedName("is_main")
    val isMain: Boolean,
    @SerializedName("car_type")
    val carType: Int,
    @SerializedName("car_model")
    val carModel: Int,
    @SerializedName("car_serie")
    val carSeries: Int,
    @SerializedName("car_modification")
    val carModification: Int,
    @SerializedName("car_generation")
    val carGeneration: Int,
    @SerializedName("car_mark")
    val carMark: Int,
    @SerializedName("car_equipment")
    val carEquipment: Int
)

data class CarCreated(
    val id: Int,
    @SerializedName("state_number")
    val stateNumber: String,
    val vin: String,
    val drivers: List<Int>,
    @SerializedName("main_driver")
    val mainDriver: Int,
    val srts: String,
    @SerializedName("is_main")
    val isMain: Boolean,
    val title: String,
    val mileage: String?,
    val age: Int,
    @SerializedName("car_color")
    val color: Int?,
    @SerializedName("car_type")
    val type: Int?,
    @SerializedName("car_mark")
    val mark: Int?,
    @SerializedName("car_model")
    val model: Int?,
    @SerializedName("car_generation")
    val generation: Int?,
    @SerializedName("car_serie")
    val serie: Int?,
    @SerializedName("car_modification")
    val modification: Int?,
    @SerializedName("car_equipment")
    val equipment: Int?
)

data class CarTitleUpdate(val title: String)

data class CarDrivers(val drivers: List<Int>)