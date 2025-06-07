package com.aryaersi0120.laporkerusakan.network

import com.aryaersi0120.laporkerusakan.model.LoginRequest
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://apikerusakan.free.nf/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface KerusakanApiService {
    @POST("login.php")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): String
}

object KerusakanApi {
    val retrofitService: KerusakanApiService by lazy {
        retrofit.create(KerusakanApiService::class.java)
    }
}