package com.aryaersi0120.laporkerusakan.ui.screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryaersi0120.laporkerusakan.network.KerusakanApiService
import com.aryaersi0120.laporkerusakan.network.LaporApi
import com.aryaersi0120.laporkerusakan.network.UserDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _logoutSuccess = kotlinx.coroutines.flow.MutableStateFlow(false)
    val logoutSuccess = _logoutSuccess


    public fun retrieveData(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = LaporApi.service.getAllLapor(userEmail)
                Log.d("MainViewModel", "Data retrieved successfully: $result items")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error retrieving data: ${e.message}")

            }
        }
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
}

