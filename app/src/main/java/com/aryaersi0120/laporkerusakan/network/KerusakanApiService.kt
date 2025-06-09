package com.aryaersi0120.laporkerusakan.network

import com.aryaersi0120.laporkerusakan.model.AuthTokenResponse
import com.aryaersi0120.laporkerusakan.model.GeneralApiResponse
import com.aryaersi0120.laporkerusakan.model.ResponseKerusakan
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://apikerusakan.free.nf/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface KerusakanApiService {
    @GET("api.php")
    suspend fun getallimage(
        @Header("Authorization") userEmail : String
    ): ResponseKerusakan

    @Multipart
    @POST("api.php")
    suspend fun uploadImage(
        @Header("Authorization") userEmail: String,
        @Part("nama_barang") nama_barang: String,
        @Part("deskripsi_kerusakan") deskripsi_kerusakan : String,
        @Part gambar : MultipartBody.Part?
    ) : GeneralApiResponse


    @Multipart
    @POST("api.php")
    suspend fun updateImage(
        @Header("Authorization") userEmail : String,
        @Query("id") imageId : Int,
        @Part("nama_barang") nama_barang : String,
        @Part("deskripsi_kerusakan") deskripsi_keruskan : String,
        @Part gambar : MultipartBody.Part?
    ): GeneralApiResponse

    @DELETE("api.php")
    suspend fun deleteImage(
        @Header("Authorization") userEmail : String,
        @Query("id") imageId : Int
    ): GeneralApiResponse
}

object ImageApi{
    val service : KerusakanApiService by lazy{
        retrofit.create(KerusakanApiService::class.java)
    }
}

enum class ApiStatus{

    LOADING, SUCCESS, FAILED
}

