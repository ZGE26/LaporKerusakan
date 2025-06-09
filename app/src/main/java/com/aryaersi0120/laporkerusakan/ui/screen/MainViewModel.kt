package com.aryaersi0120.laporkerusakan.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryaersi0120.laporkerusakan.network.UserDataStore
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _logoutSuccess = kotlinx.coroutines.flow.MutableStateFlow(false)
    val logoutSuccess = _logoutSuccess

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

