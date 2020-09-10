package com.autonomad.network

import com.autonomad.data.models.Address
import com.autonomad.data.models.AddressCreate
import com.autonomad.data.models.FcmId
import com.autonomad.data.models.Page
import com.autonomad.data.models.login.*
import com.autonomad.data.models.login.Message
import com.autonomad.data.models.payment.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiCaps {
    @POST("users/auth/")
    fun login(@Body auth: Auth): Single<Response<Token>>

    @POST("users/register/")
    fun register(@Body registration: Registration): Single<Response<UserInfo>>

    @POST("users/send-sms-code/")
    fun sendSms(@Body phone: Phone): Single<Response<Message>>

    @POST("users/verify/")
    fun verify(@Body phoneWithCode: PhoneWithCode): Single<Response<Token>>

    @POST("users/forgot-password/send-code/")
    fun forgotPasswordSendSms(@Body phone: Phone): Single<Response<Message>>

    @POST("users/forgot-password/token-by-code/")
    fun getToken(@Body phoneWithCode: PhoneWithCode): Single<Response<Token>>

    @PUT("users/forgot-password/change/")
    fun changePassword(@Body password: Password, @Header("Authorization") auth: String): Single<Response<Message>>

    @GET("payments/cloudpayments/pay/get-pay-credentials/{order_id}/")
    fun getCredentials(
        @Header("Authorization") auth: String,
        @Path("order_id") order_id: String
    ): Observable<Response<Credentials>>

    @GET("payments/cloudpayments/pay/get-pay-credentials/{order_id}/")
    fun getCredentials(@Path("order_id") orderId: String): Single<Response<Credentials>>

    @POST("payments/cloudpayments/pay/charge-no-saved-card/{order_id}/")
    fun payWithNoSavedCard(
        @Header("Authorization") auth: String,
        @Path("order_id") order_id: String,
        @Body checkout: Checkout
    ): Observable<Response<PaymentResult>>

    @POST("payments/cloudpayments/pay/charge-no-saved-card/{order_id}/")
    fun payWithNoSavedCard(@Path("order_id") orderId: String, @Body checkout: Checkout): Single<Response<PaymentResult>>

    @POST("payments/cloudpayments/pay/charge-with-saved-card/{order_id}/")
    fun payWithSavedCard(
        @Header("Authorization") auth: String,
        @Path("order_id") order_id: String,
        @Body cardID: CardID
    ): Observable<Response<PaymentResult>>

    @POST("payments/cloudpayments/pay/charge-with-saved-card/{order_id}/")
    fun payWithSavedCard(@Path("order_id") order_id: String, @Body cardID: CardID): Single<Response<PaymentResult>>

    @POST("payments/cloudpayments/pay/mobile-3d-secure-confirm/")
    fun threeDSFinish(
        @Header("Authorization") auth: String,
        @Body threeDSFinish: ThreeDSFinish
    ): Observable<Response<ResponseBody>>

    @POST("payments/cloudpayments/pay/mobile-3d-secure-confirm/")
    fun threeDSFinish(@Body threeDSFinish: ThreeDSFinish): Single<Response<ResponseBody>>

    @GET("payments/cloudpayments/cards/")
    fun getBankingCards(@Header("Authorization") auth: String): Observable<Response<BankingCard>>

    @GET("payments/cloudpayments/cards/")
    fun getBankingCards(): Single<Response<Page<BankingCardResult>>>

    @GET("users/profile/")
    fun getProfile(@Header("Authorization") auth: String): Single<Response<Profile>>

    @GET("users/profile/")
    fun getProfile(): Single<Response<Profile>>

    @GET("users/profile/")
    suspend fun loadProfile(): Response<Profile>

    @GET("geo/addresses/")
    fun getAddresses(): Single<Response<List<Address>>>

    @GET("geo/addresses/{id}/")
    fun getAddress(@Path("id") id: Int): Single<Response<Address>>

    @POST("geo/addresses/")
    fun addAddress(@Body address: AddressCreate): Single<Response<AddressCreate>>

    @PATCH("geo/addresses/{id}/")
    fun updateAddress(@Path("id") id: Int, @Body address: AddressCreate): Single<Response<AddressCreate>>

    @DELETE("geo/addresses/{id}/")
    fun deleteAddress(@Path("id") id: Int): Single<Response<ResponseBody>>

    @GET("geo/cities/")
    fun getCities(
        @Query("search") search: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Single<Response<Page<City>>>

    @GET("geo/cities/{id}/")
    fun getCity(@Path("id") id: Int): Single<Response<City>>

    @PATCH("users/profile/")
    fun updateCity(@Body body: CityUpdate): Single<Response<ResponseBody>>

    @PATCH("users/profile/")
    fun updateUser(@Header("Authorization") auth: String, @Body body: UserUpdate): Single<Response<Profile>>

    @GET("geo/countries")
    fun getCountries(@Query("search") search: String): Single<Response<Page<Country>>>

    @GET("get/countries/{id}")
    fun getCountry(@Path("id") id: Int): Single<Response<Country>>

    @Multipart
    @PUT("users/upload-avatar/")
    fun uploadAvatar(@Header("Authorization") auth: String, @Part image: MultipartBody.Part): Single<Response<ResponseBody>>

    @POST("notifications/devices/")
    suspend fun sendFcmId(@Body fcmId: FcmId): Response<FcmId>
}