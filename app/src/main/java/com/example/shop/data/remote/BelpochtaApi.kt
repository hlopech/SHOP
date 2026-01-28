package com.example.shop.data.remote

import com.example.shop.domain.model.ApiResponse
import com.example.shop.domain.model.PackagePayload
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BelpochtaApi {
    @POST("packages")
    suspend fun addPackage(
        @Header("X-Api-Key") token: String,
        @Query("trackNumber") trackNumber: String,
        @Query("courierId") courierId: Int = 13,
        @Query("recipientName") recipientName: String,
        @Query("recipientPhone") recipientPhone: String,
        @Query("destinationAddress") destinationAddress: String
    ): ApiResponse<PackagePayload>

    @GET("packages")
    suspend fun getPackage(
        @Header("X-Api-Key") token: String,
        @Query("trackNumber") trackNumber: String
    ): ApiResponse<PackagePayload>
}
object NetworkModule {
    private const val BASE_URL = "https://business.pkge.ne/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val belpochtaApi: BelpochtaApi by lazy {
        retrofit.create(BelpochtaApi::class.java)
    }
}