package com.autonomad.network

import com.autonomad.data.models.Page
import com.autonomad.data.models.Story
import com.autonomad.data.models.check_auto.*
import com.autonomad.data.models.insurance.InsuranceCheck
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.data.models.insurance.NewCheck
import com.autonomad.data.models.main_page_car.*
import com.autonomad.data.models.main_page_car.GetItems
import com.autonomad.data.models.main_page_car.Result
import com.autonomad.data.models.parking.*
import com.autonomad.data.models.penalty.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiGarage {
    @GET("garage/cars/")
    fun getCars(@Header("Authorization") auth: String): Single<Response<Cars>>

    @GET("garage/cars/")
    fun getGarageCars(
        @Query("limit") limit: Int = 1000,//todo в некоторых местах пагинация не реализовано и пока так оставим
        @Query("offset") offset: Int = 0
    ): Single<Response<Page<GarageCar>>>

    @GET("garage/cars/")
    suspend fun getGarageCarsList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<Page<GarageCar>>

    @GET("garage/cars/{id}/")
    fun getCarByID(@Path("id") id: Int): Single<Response<Result>>

    @PATCH("garage/cars/{id}/")
    fun patchIsMain(@Path("id") id: Int, @Body is_main: IsMainUpdate): Single<Response<CarUpdated>>

    @GET("garage/cars/{id}/")
    suspend fun getCarDetails(@Path("id") id: Int): Response<CarDetails>

    @GET("checkauto/m_lookup/{identification}")
    fun getCarLookup(
        @Path("identification") stateNumber: String
    ): Single<Response<CarLookup>>

    @GET("garage/stories")
    fun getStories(): Single<Response<Page<Story>>>

    @GET("cars/marks/")
    fun getNameOfMarks(
        @Header("Authorization") auth: String,
        @Query("mark_name") name_contains: String,
        @Query("limit") limit: Int
    ): Observable<Response<GetItems>>

    @GET("cars/models/")
    fun getNameOfModels(
        @Header("Authorization") auth: String,
        @Query("car_mark_id") car_mark_id: Int,
        @Query("model_name") name_contains: String,
        @Query("limit") limit: Int
    ): Observable<Response<GetItems>>

    @GET("garage/colors/")
    fun getColors(): Observable<Response<GetItems>>

    @GET("cars/generations/")
    fun getNameOfGenerations(
        @Header("Authorization") auth: String,
        @Query("car_model_id") car_model_id: Int,
        @Query("limit") limit: Int
    ): Observable<Response<GetItems>>

    @GET("cars/series/")
    fun getSeries(
        @Header("Authorization") auth: String,
        @Query("car_generation_id") car_generation_id: Int,
        @Query("limit") limit: Int
    ): Observable<Response<GetItems>>

    @GET("cars/modifications/")
    fun getNameOfModifications(
        @Header("Authorization") auth: String,
        @Query("car_serie_id") car_serie_id: Int,
        @Query("modification_name") name_contains: String,
        @Query("limit") limit: Int
    ): Observable<Response<GetItems>>

    @GET("garage/tech-inspection/")
    fun getInspections(): Single<Response<Page<Inspection>>>

    @DELETE("garage/tech-inspection/{id}/")
    fun deleteTechInspection(@Path("id") id: Int): Single<Response<ResponseBody>>

    @POST("garage/tech-inspection/")
    fun getInspection(@Body carId: CarId): Single<Response<Inspection>>

    @POST("garage/tech-inspection/check/")
    fun checkInspection(@Body car: CarStateNumber): Single<Response<Inspection>>

    @POST("garage/cars/")
    fun setCar(
        @Header("Authorization") auth: String,
        @Body addCar: addCar
    ): Observable<Response<ResponseBody>>

    @POST("garage/cars/")
    fun setCar(@Body body: CarStateNumber): Single<Response<CarCreated>>

    @POST("garage/cars/")
    fun setCar(@Body body: CarVin): Single<Response<CarCreated>>

    @PATCH("garage/cars/{id}/")
    fun updateSrts(@Path("id") carId: Int, @Body srts: SrtsUpdate): Single<Response<CarUpdated>>

    @PATCH("garage/cars/{id}/")
    fun updateCarTitle(@Path("id") carId: Int, @Body body: CarTitleUpdate): Single<Response<CarUpdated>>

    @PATCH("garage/cars/{id}/")
    suspend fun setCarDrivers(@Path("id") carId: Int, @Body drivers: CarDrivers): Response<CarUpdated>

    @PUT("garage/cars/{id}/")
    fun setCar(@Path("id") id: Int, @Body addCar: addCar): Observable<Response<ResponseBody>>

    @GET("checkauto/m_presearch/{identification}/")
    suspend fun getCarByVinDetail(@Path("identification") identification: String): Response<Report>

    @DELETE("garage/cars/{id}/")
    fun deleteCar(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<ResponseBody>>

    @DELETE("garage/cars/{id}/")
    fun deleteCar(@Path("id") id: Int): Single<Response<ResponseBody>>

    @DELETE("garage/cars/{id}/")
    suspend fun deleteCarFromGarage(@Path("id") id: Int): Response<ResponseBody>

    @GET("checkauto/mass-avg-price/{state_number}/")
    fun getAvgPrice(@Path("state_number") stateNumber: String): Single<Response<AvgPrice>>

    @GET("checkauto/orders/")
    fun getCheckAutoHistory(): Single<Response<Page<CheckAutoHistory>>>

    @GET("checkauto/orders/")
    suspend fun getCheckAutoOrders(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<Page<CheckAutoHistory>>

    @GET("checkauto/orders/{id}/")
    suspend fun getCheckAutoDetailTicket(
        @Path("id") id: Int
    ): Response<DetailTicket>

    @POST("checkauto/orders/")
    suspend fun createOrder(@Body createOrder: CreateOrder): Response<CreateOrderStatus>

    @GET("checkauto/orders/{id}/")
    suspend fun checkOrderStatus(@Path("id") id: Int): Response<CreateOrderStatus>

    @GET("checkinsurance/favorite-insurances/")
    fun getInsuranceFavorite(): Single<Response<Page<InsuranceHistory>>>

    @GET("checkinsurance/favorite-insurances/")
    suspend fun getInsuranceFavoriteList(): Response<Page<InsuranceHistory>>

    @GET("checkinsurance/history-insurances/")
    suspend fun getInsuranceHistory(): Response<Page<InsuranceHistory>>

    @GET("checkinsurance/history-insurances/{id}/")
    fun getInsurance(@Path("id") id: Int): Single<Response<InsuranceHistory>>

    @POST("checkinsurance/v1/")
    fun checkInsurance(@Body check: NewCheck): Single<Response<InsuranceCheck>>

    @POST("aparking/check-price/")
    fun checkPrice(
        @Header("Authorization") auth: String,
        @Body checkPrice: CheckPrice
    ): Observable<Response<CheckPriceResult>>

    @GET("aparking/distinct-parking-zones/")
    fun getParkingZones(
        @Header("Authorization") auth: String
    ): Observable<Response<com.autonomad.data.models.parking.GetItems>>

    @POST("aparking/orders/")
    fun createParkingOrder(
        @Header("Authorization") auth: String,
        @Body createOrder: Create_order
    ): Observable<Response<ParkingOrder>>

    @GET("aparking/orders")
    fun getParkingOrders(
        @Header("Authorization") auth: String,
        @Query("period_type") periodType: Int?
    ): Observable<Response<History>>

    @GET("aparking/orders/{id}/")
    fun checkParkingOrder(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<ParkingOrder>>

    @GET("drivers/")
    fun getDrivers(): Single<Response<Page<Driver>>>

    @GET("drivers/")
    fun getDriversTwo(@Header("Authorization") auth: String): Observable<Response<Page<Driver>>>

    @GET("drivers/{id}/")
    suspend fun getDriver(@Path("id") id: Int): Response<Driver>

    @DELETE("drivers/{id}/")
    fun deleteDriver(@Path("id") id: Int): Single<Response<ResponseBody>>

    @DELETE("drivers/{id}/")
    suspend fun deleteDriverSuspend(@Path("id") id: Int): Response<ResponseBody>

    @DELETE("drivers/{id}/")
    fun deleteDriverrr(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Observable<Response<ResponseBody>>

    @DELETE("drivers/{id}/")
    suspend fun deleteDriverItem(@Path("id") id: Int): Response<ResponseBody>

    @PATCH("drivers/update/{id}/")
    suspend fun saveDriverRequest(@Path("id") id: Int, @Body request: SaveRequest): Response<SaveRequest>

    @POST("drivers/")
    suspend fun addDriver(@Body request: DriverCheckRequest): Response<DriverCreate>

    @POST("drivers/drivers/check/")
    fun checkDriver(@Body request: DriverCheckRequest): Single<Response<DriverCheck>>

    @PATCH("drivers/update/{id}/")
    suspend fun saveDriver(@Path("id") id: Int, @Body body: SaveRequest): Response<SaveRequest>

    @GET("penalties/search/{target}/")
    suspend fun penaltiesSearch(@Path("target") uin: String): Response<PenaltySearch>

    @GET("penalties/detail/{id}/")
    fun penaltyDetail(@Path("id") id: Int): Single<Response<PenaltyDetail>>

    @POST("penalties/orders/")
    fun makeOrder(@Body penaltyId: PenaltyId): Single<Response<MakeOrder>>

    @GET("penalties/orders/{id}/")
    fun getOrder(@Path("id") id: Int): Single<Response<OrderDetail>>

    @GET("penalties/payment-history/")
    fun getPenaltyHistory(): Single<Response<Page<PaymentHistory>>>

    @GET("penalties/payment-history/{id}/")
    fun getPenaltyHistory(@Path("id") id: Int): Single<Response<PaymentHistory>>
}
