package com.aryaersi0120.laporkerusakan.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aryaersi0120.laporkerusakan.model.Kerusakan
import com.aryaersi0120.laporkerusakan.network.ApiStatus
import com.aryaersi0120.laporkerusakan.network.KerusakanApiService
import com.aryaersi0120.laporkerusakan.network.LaporApi
import com.aryaersi0120.laporkerusakan.network.UserDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class KerusakanViewModel : ViewModel() {

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess = _logoutSuccess

    var kerusakanList = mutableStateOf(emptyList<Kerusakan>())
    private set

    var errorMessage = mutableStateOf<String?>(null)
    private set

    var apiStatus = mutableStateOf(ApiStatus.LOADING)
    private set

    fun retrieveData(UserId : String){
        viewModelScope.launch(Dispatchers.IO) {
            apiStatus.value = ApiStatus.LOADING
            try {
                val response = LaporApi.service.getAllLapor(UserId)
                kerusakanList.value = response.data
                apiStatus.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error retrieving data: ${e.message}")
                apiStatus.value = ApiStatus.FAILED
            }
        }
    }


    fun saveData(userId: String,  bitmap: Bitmap, namaBarang: String, deskripsiKerusakan: String, lokasi: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imagePart = bitmap.toMultipartBody()
                val response = LaporApi.service.uploadImage(
                    userEmail = userId,
                    nama_barang = namaBarang,
                    deskripsi_kerusakan = deskripsiKerusakan,
                    lokasi = lokasi,
                    gambar = imagePart
                )
                if (response.status == "success") {
                    Log.d("MainViewModel", "Data saved successfully")
                    retrieveData(userId) // Refresh data after saving
                } else {
                    throw Exception(response.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure ${e.message}")
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpeg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            UserDataStore(context).clearData()
            _logoutSuccess.value = true
        }
    }

    fun resetLogoutState() {
        _logoutSuccess.value = false
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}