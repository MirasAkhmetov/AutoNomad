package com.autonomad.data.models.meteo_currency

import com.google.gson.annotations.SerializedName

data class Currencies(
    val message: List<Currency>
)

data class Currency(
    val title: String,
    @SerializedName("pub_date") val pubDate: String,
    val description: String,
    val quant: String,
    val index: String,
    val change: String,
    val link: String?
)