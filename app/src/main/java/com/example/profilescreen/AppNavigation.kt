package com.example.profilescreen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.profilescreen.auth.signin.LoginScreen
import com.example.profilescreen.auth.signup.SignUpScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Login.route
    ) {
        // Login Screen
        composable(AppRoute.Login.route) {
            LoginScreen(navController)
        }

        // SignUp Screen
        composable(AppRoute.SignUp.route) {
            SignUpScreen(navController)
        }

        // Main App with Bottom Navigation
        composable(AppRoute.Home.route) {
            MainAppScreen(navController)
        }
    }
}