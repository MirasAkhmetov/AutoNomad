package com.autonomad.data.models.parking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class pageInfo(
    var pin: String,
    val hours: String,
    val min: String,
    val bankingCard: String,
    val carNumber: String,
    var price: String
)

data class Create_order(
    @SerializedName("zone_num")
    @Expose
    val zoneNum: String,
    @SerializedName("car_num")
    @Expose
    val carNum: String,
    @SerializedName("minute")
    @Expose
    val minute: String
)

data class CheckPrice(
    @SerializedName("zone_num")
    @Expose
    val zoneNum: String,
    @SerializedName("car_num")
    @Expose
    val carNum: String,
    @SerializedName("minute")
    @Expose
    val minute: String
)

data class GetItems(
    @SerializedName("parking_zones")
    @Expose
    val parkingZones: List<ParkingZone>
)

data class ParkingZone(
    @SerializedName("number")
    @Expose
    val number: String,
    @SerializedName("avg_lat")
    @Expose
    val avgLat: Double,
    @SerializedName("avg_lon")
    @Expose
    val avgLon: Double
)

class CheckPriceResult(
    @SerializedName("status")
    @Expose
    val status: Int,
    @SerializedName("operation")
    @Expose
    val operation: String,
    @SerializedName("version")
    @Expose
    val version: String,
    @SerializedName("response")
    @Expose
    val response: Response
) {
    data class Response(
        @SerializedName("carNo")
        @Expose
        val carNo: String,
        @SerializedName("zoneID")
        @Expose
        val zoneID: String,
        @SerializedName("placeID")
        @Expose
        val placeID: String,
        @SerializedName("placeType")
        @Expose
        val placeType: String,
        @SerializedName("placeUniqueID")
        @Expose
        val placeUniqueID: String,
        @SerializedName("placeOwnerUniqueID")
        @Expose
        val placeOwnerUniqueID: Any,
        @SerializedName("startTime")
        @Expose
        val startTime: Int,
        @SerializedName("stopTime")
        @Expose
        val stopTime: Int,
        @SerializedName("totalCharge")
        @Expose
        val totalCharge: Int,
        @SerializedName("totalChargeText")
        @Expose
        val totalChargeText: String,
        @SerializedName("zoneType")
        @Expose
        val zoneType: String,
        @SerializedName("tariffDescription_kk")
        @Expose
        val tariffDescriptionKk: String,
        @SerializedName("tariffDescription_ru")
        @Expose
        val tariffDescriptionRu: String,
        @SerializedName("tariffDescription_en")
        @Expose
        val tariffDescriptionEn: String,
        @SerializedName("language")
        @Expose
        val language: String
    )
}

data class History(
    @SerializedName("count")
    @Expose
    val count: Int,
    @SerializedName("next")
    @Expose
    val next: String,
    @SerializedName("previous")
    @Expose
    val previous: String,
    @SerializedName("results")
    @Expose
    val results: List<ParkingOrder>
)

data class ParkingOrder(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("order_id")
    @Expose
    val orderId: String,
    @SerializedName("card")
    @Expose
    val card: Card,
    @SerializedName("purchased_at")
    @Expose
    val purchasedAt: Any,
    @SerializedName("created_at")
    @Expose
    val createdAt: String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String,
    @SerializedName("parking")
    @Expose
    val parking: Parking,
    var show: Boolean,
    var date1: String,
    var date2: String
) {
    data class Card(
        @SerializedName("card_last_four")
        @Expose
        val cardLastFour: String,
        @SerializedName("card_type")
        @Expose
        val cardType: String
    )

    data class Parking(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("zone_num")
        @Expose
        val zoneNum: String,
        @SerializedName("car_num")
        @Expose
        val carNum: String,
        @SerializedName("paid")
        @Expose
        val paid: String,
        @SerializedName("minute")
        @Expose
        val minute: String,
        @SerializedName("session_id")
        @Expose
        val sessionId: Any,
        @SerializedName("order")
        @Expose
        val order: Int
    )
}