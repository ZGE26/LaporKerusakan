package com.aryaersi0120.laporkerusakan.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryaersi0120.laporkerusakan.network.DummyKerusakanAPI
import com.aryaersi0120.laporkerusakan.network.KerusakanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    public fun login(email:String, password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = KerusakanApi.retrofitService.login(email, password)
                Log.d("Login Result", "Welcome ${result.access_token}" )
            } catch (e: Exception) {
                Log.e("Login Error", "Error logging in: ${e.message}")
            }
        }
    }
    fun loginDummy(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = DummyKerusakanAPI.login(email, password)

                if (response != null) {
                    Log.d("Login Result", "Token: ${response.access_token}, Type: ${response.token_type}")
                } else {
                    Log.d("Login Failed", "Email atau password salah")
                }

            } catch (e: Exception) {
                Log.e("Login Error", "Error logging in: ${e.message}")
            }
        }
    }

}