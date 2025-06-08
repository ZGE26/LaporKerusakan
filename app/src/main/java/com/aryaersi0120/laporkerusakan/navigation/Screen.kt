package com.aryaersi0120.laporkerusakan.navigation

sealed class Screen(val route:String) {
    data object LoginScreen: Screen("login_screen")
    data object RegisterScreen: Screen("register_screen")
    data object MainScreen: Screen("main_screen")
    data object LoadingScreen: Screen("loading_screen")
}