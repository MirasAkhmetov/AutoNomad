package com.autonomad.data.models.claims

import com.google.gson.annotations.SerializedName

data class SpecialistStatistics(
    @SerializedName("view_count") val viewCount: List<List<String>>,
    @SerializedName("service_response_count_list") val serviceResponseCount: List<List<String>>,
    val funnel: Funnel
)

data class Funnel(@SerializedName("given_responses") val responses: Int, @SerializedName("done_works") val completed: Int)