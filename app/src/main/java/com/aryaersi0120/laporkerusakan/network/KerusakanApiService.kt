package com.aryaersi0120.laporkerusakan.network

import com.aryaersi0120.laporkerusakan.model.AuthTokenResponse
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.POST
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
    ): AuthTokenResponse
}

object DummyKerusakanAPI {
    suspend fun login(email: String, password: String): AuthTokenResponse? {
        return if (email == "test@gmail.com" && password == "12345678") {
            AuthTokenResponse(
                access_token = "dummy_token_1234567890",
                token_type = "Bearer"
            )
        } else {
            null
        }
    }
}


object KerusakanApi {
    val retrofitService: KerusakanApiService by lazy {
        retrofit.create(KerusakanApiService::class.java)
    }
}