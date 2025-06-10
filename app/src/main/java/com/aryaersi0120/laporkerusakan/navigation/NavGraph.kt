package com.aryaersi0120.laporkerusakan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryaersi0120.laporkerusakan.ui.screen.LoadingScreen
import com.aryaersi0120.laporkerusakan.ui.screen.LoginScreen
import com.aryaersi0120.laporkerusakan.ui.screen.MainScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController,
        startDestination = Screen.LoadingScreen.route
    ) {
        composable(route = Screen.LoadingScreen.route) {
            LoadingScreen(navController)
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController)
        }
    }
}