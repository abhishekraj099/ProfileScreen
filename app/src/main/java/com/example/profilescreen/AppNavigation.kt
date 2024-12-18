package com.example.profilescreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.profilescreen.auth.signin.LoginScreen
import com.example.profilescreen.auth.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Determine user authentication state
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isSignedIn = currentUser != null

    var isNewUser by remember { mutableStateOf(false) }
    if (isSignedIn) {
        val userId = currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        // Fetch user data from Firebase Realtime Database
        LaunchedEffect(key1 = userId) {
            database.child("users").child(userId!!).get().addOnCompleteListener { task ->
                isNewUser = !task.isSuccessful || task.result.value == null
            }
        }
    }

    // Determine start destination dynamically
    val startDestination = when {
        !isSignedIn -> AppRoute.Login.route
        isNewUser -> AppRoute.Details.route
        else -> AppRoute.Home.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(AppRoute.Login.route) {
            LoginScreen(navController)
        }

        // SignUp Screen
        composable(AppRoute.SignUp.route) {
            SignUpScreen(navController)
        }

        // Details Screen
        composable(AppRoute.Details.route) {
            DetailsScreen(navController)
        }

        // Main App with Bottom Navigation
        composable(AppRoute.Home.route) {
            MainAppScreen(navController)
        }
    }
}

