package com.autonomad.data.models.meteo_currency

import com.autonomad.data.models.main_page_car.Items

data class Weather(
    var latitude: String,
    var longitude: String,
    var main: String,
    var description: String,
    var temp: String,
    var icon: Items
)