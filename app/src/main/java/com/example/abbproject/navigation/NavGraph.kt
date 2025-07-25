package com.example.abbproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.abbproject.ui.screens.emailverification.EmailVerifyScreen
import com.example.abbproject.ui.screens.login.LoginScreen
import com.example.abbproject.ui.screens.register.RegisterScreen
import com.example.abbproject.ui.screens.splash.SplashScreen
import com.example.abbproject.ui.screens.home.HomeScreen
import com.example.abbproject.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController)
        }
        composable(Routes.Home.route) {
            HomeScreen(navController)
        }
        composable(Routes.Emailverify.route) {
            EmailVerifyScreen(navController)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController)
        }
    }
}
