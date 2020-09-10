package com.autonomad.data.models.claims

import com.autonomad.data.models.Status
import com.autonomad.data.models.check_auto.GarageCar
import com.google.gson.annotations.SerializedName

data class ServiceResponse(
    val id: Int,
    val status: Status,
    @SerializedName("service_request") val request: ServiceRequest,
    val text: String,
    val car: GarageCar?
)

data class ResponseUpdate(val status: Int)