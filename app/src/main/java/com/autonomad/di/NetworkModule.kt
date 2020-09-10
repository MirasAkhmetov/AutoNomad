package com.autonomad.di

import com.autonomad.App
import com.autonomad.network.ApiShop
import com.autonomad.utils.Constants
import com.autonomad.utils.Methods
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single { getOkHttpClient() }
    single { getRetrofit(get()) }
    single { createShopService(get()) }
}

fun createShopService(retrofit: Retrofit): ApiShop {
    return retrofit.create(ApiShop::class.java)
}

fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL_SHOP)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()

}

fun getOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(ChuckInterceptor(App.CONTEXT))
        .addInterceptor(interceptor)
        .addInterceptor {
            val requestBuilder = it.request().newBuilder()
                .addHeader("Authorization", Methods.getToken())
            it.proceed(requestBuilder.build())
        }
        .connectTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .build()
}