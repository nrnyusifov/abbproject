package com.example.abbproject.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Register : Routes("register")
    object Home : Routes("home")
    object Emailverify : Routes("email_verification")
    object Profile : Routes("profile")
    object Account: Routes("account")
}