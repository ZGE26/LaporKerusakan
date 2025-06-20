package com.aryaersi0120.laporkerusakan.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.aryaersi0120.laporkerusakan.navigation.Screen
import com.aryaersi0120.laporkerusakan.network.UserDataStore
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userDataStore = UserDataStore(context)
    val user by userDataStore.userFlow.collectAsState(initial = null)

    LaunchedEffect(user) {
        if (user != null) {
            delay(800)

            if (user?.email.isNullOrEmpty()) {
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.LoadingScreen.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.LoadingScreen.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

