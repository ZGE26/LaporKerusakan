package com.aryaersi0120.laporkerusakan.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryaersi0120.laporkerusakan.network.KerusakanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    public fun login(email:String, password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = KerusakanApi.retrofitService.login(email, password)
                Log.d("Login Result", "${result.toString()}")
            } catch (e: Exception) {
                Log.e("Login Error", "Error logging in: ${e.message}")
            }
        }
    }
}