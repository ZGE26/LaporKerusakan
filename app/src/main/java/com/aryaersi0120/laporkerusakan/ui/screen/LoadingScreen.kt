package com.aryaersi0120.laporkerusakan.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.aryaersi0120.laporkerusakan.navigation.Screen
import com.aryaersi0120.laporkerusakan.network.TokenPreference
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val token by TokenPreference.getToken(context).collectAsState(initial = null)

    LaunchedEffect(token) {
        delay(500)
        if (token.isNullOrEmpty()) {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.LoadingScreen.route)
            }
        } else {
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(Screen.LoadingScreen.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}