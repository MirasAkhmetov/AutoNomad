package com.autonomad.network

import com.autonomad.data.models.meteo_currency.City
import com.autonomad.data.models.meteo_currency.Currencies
import com.autonomad.data.models.meteo_currency.Weather
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMeteoCurrency {
    @GET("currency")
    fun getCurrency(): Single<Response<Currencies>>

    @GET("meteo/")
    fun getWeather(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Single<Response<Weather>>

    @GET("fuel/nearest/")
    fun getFuelNearest(@Query("id") id: Int): Single<Response<City>>
}
