package com.autonomad.network

import com.autonomad.data.models.Page
import com.autonomad.data.models.ServiceResponseCreate
import com.autonomad.data.models.claims.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiRequests {
    @GET("categories/")
    fun getCategories(): Single<Response<Page<Category>>>

    @GET("categories/{id}/")
    fun getCategoryById(@Path("id") id: Int): Single<Response<Category>>

    @GET("service/service-requests/")
    fun getServiceRequests(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<Response<Page<ServiceRequest>>>

    @GET("service/service-requests/")
    fun getServiceRequestsWithFilter(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("sub_categories") subcategory: Int?,
        @Query("by_newest") byNewest: Boolean?,
        @Query("price_up") priceUp: String?,
        @Query("price_down") priceDown: String?,
        @Query("latitude") latitude: String?,
        @Query("longitude") longitude: String?
    ): Single<Response<Page<ServiceRequest>>>

    @GET("/masters/me/service-responses/")
    fun getServiceResponses(
        @Query("status") status: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Single<Response<Page<ServiceResponse>>>

    @GET("/masters/me/service-responses/{id}")
    fun getServiceResponse(@Path("id") id: Int): Single<Response<ServiceResponse>>

    @GET("/masters/me/service-responses/{id}")
    fun updateServiceResponse(@Path("id") id: Int, @Body body: ResponseUpdate): Single<Response<ServiceResponse>>

    @GET("service/my/service-requests/{id}/")
    fun getMyServiceRequestById(@Path("id") id: Int): Single<Response<ServiceRequest>>

    @GET("service/service-requests/{id}/")
    fun getServiceRequestById(@Path("id") id: Int): Single<Response<ServiceRequest>>

    @POST("service/service-requests/{id}/make-response/")
    fun makeResponse(@Path("id") id: Int, @Body body: ServiceResponseCreate): Single<Response<ServiceResponseCreate>>

    @GET("masters/masters/{id}/reviews")
    fun getReviews(
        @Path("id") masterId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Single<Response<Page<Review>>>

    @GET("masters/me/")
    fun logSpecialist(): Single<Response<Specialist>>

    @POST("masters/me/service-offers/")
    fun addOffers(@Body offer: CreateOffer): Single<Response<CreateOffer>>

    @DELETE("masters/me/service-offers/{id}/")
    fun deleteServiceOffer(@Path("id") offerId: Int): Single<Response<ResponseBody>>

    @PUT("masters/me/service-offers/{id}/")
    fun updateServiceOffer(@Path("id") offerId: Int, @Body offer: CreateOffer): Single<Response<CreateOffer>>

    @Multipart
    @POST("/masters/me/service-images/upload/")
    fun uploadImage(@Part image: MultipartBody.Part): Single<Response<ResponseBody>>

    @DELETE("masters/me/service-images/{id}/")
    fun deleteImage(@Path("id") imageId: Int): Single<Response<ResponseBody>>

    @GET("rates/fabrics/")
    fun getTariffs(): Single<Response<Page<Tariff>>>

    @POST("rates/orders/")
    fun orderTariff(@Body body: TariffPost): Single<Response<TariffCreated>>

    @GET("rates/orders/{id}/")
    fun getTariffOrder(@Path("id") orderId: Int): Single<Response<TariffOrder>>

    @GET("/masters/me/view-analytics/")
    fun analytics(): Single<Response<SpecialistStatistics>>
}