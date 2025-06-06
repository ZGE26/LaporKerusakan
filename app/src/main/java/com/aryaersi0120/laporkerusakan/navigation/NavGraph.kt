package com.aryaersi0120.laporkerusakan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryaersi0120.laporkerusakan.ui.screen.LoginScreen
import com.aryaersi0120.laporkerusakan.ui.screen.MainScreen
import com.aryaersi0120.laporkerusakan.ui.screen.RegisterScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
    ) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.MainScreen.route) {
            MainScreen()
        }
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController)
        }
    }
}