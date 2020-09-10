package com.autonomad.network

import com.autonomad.data.models.claim_user.*
import com.autonomad.data.models.claims.MasterComplaintRequest
import com.autonomad.data.models.claims.ServiceModel
import com.autonomad.data.models.login.Message
import com.autonomad.data.models.main_page_car.IsActivUpdate
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiAutoReg{
    @GET("categories/")
    fun getCategories(@Header("Authorization") auth: String): Observable<Response<Categories>>

    @GET("categories/{id}/")
    fun getCategoriesId(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<CategoriesId>>

    @GET("masters/top/")
    fun getTopMasters(@Header("Authorization") auth: String): Observable<Response<Masters>>

    @GET("service/my/service-requests/{id}/responses/")
    fun getMastersRes(@Header("Authorization") auth: String,
                      @Path("id") id: String): Observable<Response<MastersList>>

    @GET("masters/masters/")
    fun getMastersClaims(
        @Header("Authorization") auth: String,
        @Query("subcategory") subCategoryId: Int,
        @Query("rating") sortByRating: Boolean?,
        @Query("popularity") sortByPopularity: Boolean?
    ): Observable<Response<Masters>>

    @GET("masters/masters/{id}/")
    fun getMastersId(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<MastersId>>
    @GET("masters/masters/{id}/reviews/")
    fun getMastersReview(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<MastersReview>>

    @POST("masters/complaints/")
    fun complaintMaster(
        @Header("Authorization") auth: String,
        @Body complaintInfo: MasterComplaintRequest
    ): Observable<Response<MasterComplaintRequest>>

    @GET("service/my/service-requests/{id}/responses/")
    fun getSubCat(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<MastersList>>

    @GET("masters/favourite-masters/")
    fun getFubCat(
        @Header("Authorization") auth: String
    ): Observable<Response<Masters>>
    @POST("masters/favourite-masters/")
    fun postFavour(
        @Header("Authorization") auth: String,
        @Body mastID: MastID
    ): Observable<Response<MastID>>
    @DELETE("masters/favourite-masters/{id}/")
    fun deleteFavour(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<MastID>>

    @Multipart
    @POST("/service/my/upload-image/") // service/my/upload-image/   /masters/me/service-images/upload/
    fun uploadImage(@Header("Authorization") auth: String, @Part image: MultipartBody.Part): Single<Response<UplooodImage>>

    @GET("service/my/service-requests/")
    fun getServicereq(@Header("Authorization") auth: String): Observable<Response<Servicesreq>>
    @DELETE("service/my/service-requests/{id}/")
    fun deleteServicereq(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<Message>>
    @GET("service/my/service-requests/{id}/")
    fun getServiceReqId(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<ServiceModel>>

    @POST("service/my/service-requests/")
    fun serviceCreate(
        @Header("Authorization") auth: String,
        @Body serviceCreate: ServiceCreate
    ): Observable<Response<ServiceCreate>>

    @PATCH("service/my/service-requests/{id}/")
    fun patchIsActiv(@Header("Authorization") auth: String, @Path("id") id: Int, @Body int_status: IsActivUpdate): Observable<Response<Message>>

    @PATCH("service/my/service-requests/{id}/")
    fun editCreate(@Header("Authorization") auth: String, @Path("id") id: Int, @Body claim: ServiceCreate): Observable<Response<Message>>
}