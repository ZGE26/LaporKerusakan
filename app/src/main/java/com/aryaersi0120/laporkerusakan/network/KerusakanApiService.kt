package com.aryaersi0120.laporkerusakan.network

import com.aryaersi0120.laporkerusakan.model.GeneralApiResponse
import com.aryaersi0120.laporkerusakan.model.Kerusakan
import com.aryaersi0120.laporkerusakan.model.ResponseKerusakan
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://kogenkode.my.id//"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KerusakanApiService {
    @GET("api.php")
    suspend fun getAllLapor(
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

object LaporApi{
    val service : KerusakanApiService by lazy{
        retrofit.create(KerusakanApiService::class.java)
    }

    fun getKerusakanUrl(imagePath: String): String {
        return BASE_URL + imagePath
    }
}

enum class ApiStatus{ LOADING, SUCCESS, FAILED }

