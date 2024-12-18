package com.example.profilescreen

sealed class AppRoute(val route: String) {
    object Login : AppRoute("login")
    object SignUp : AppRoute("signup")
    object Home : AppRoute("home")
    object Explore : AppRoute("explore")
    object Community : AppRoute("community")
    object Profile : AppRoute("profile")
    object Details : AppRoute("details")
}