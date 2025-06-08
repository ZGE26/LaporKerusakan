package com.aryaersi0120.laporkerusakan.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.aryaersi0120.laporkerusakan.navigation.Screen
import com.aryaersi0120.laporkerusakan.network.DummyKerusakanAPI
import com.aryaersi0120.laporkerusakan.network.KerusakanApi
import com.aryaersi0120.laporkerusakan.network.TokenPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    var loginSuccess by mutableStateOf(false)
        private set

    var logoutSuccess by mutableStateOf(false)
        private set

    fun loginDummy(email: String, password: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = DummyKerusakanAPI.login(email, password)

                if (response != null) {
                    TokenPreference.saveToken(context, response.access_token)
                    Log.d("Login Result", "Token: ${response.access_token}, Type: ${response.token_type}")

                    withContext(Dispatchers.Main) {
                        loginSuccess = true
                    }

                } else {
                    Log.d("Login Failed", "Email atau password salah")
                }

            } catch (e: Exception) {
                Log.e("Login Error", "Error logging in: ${e.message}")
            }
        }
    }

    // MainViewModel.kt
    fun logout(context: Context, navController: NavHostController) {
        viewModelScope.launch {
            TokenPreference.logout(context)
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.MainScreen.route) {
                    inclusive = true
                }
            }
        }
    }

}