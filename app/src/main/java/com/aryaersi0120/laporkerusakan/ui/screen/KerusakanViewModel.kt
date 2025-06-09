package com.aryaersi0120.laporkerusakan.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aryaersi0120.laporkerusakan.model.Kerusakan
import com.aryaersi0120.laporkerusakan.network.ApiStatus
import com.aryaersi0120.laporkerusakan.network.KerusakanApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KerusakanViewModel : ViewModel() {

    var imageList = mutableStateOf(emptyList<Kerusakan>())
    private set

    var errorMessage = mutableStateOf<String?>(null)
    private set

    var apiStatis = mutableStateOf(ApiStatus.LOADING)
    private set

    fun clearMessage(){
        errorMessage.value = null
    }

    fun retrieveData(UserId : String){
        viewModelScope.launch(Dispatchers.IO)
        {

        }

    }

    fun saveData(userId: String, nama: String, namaLatin: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure ${e.message}")
            }
        }
    }

}