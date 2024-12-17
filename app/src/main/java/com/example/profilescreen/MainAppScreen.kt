package com.example.profilescreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(navController: NavController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavController) }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = AppRoute.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppRoute.Home.route) { HomeScreen() }
            composable(AppRoute.Explore.route) { ExploreScreen() }
            composable(AppRoute.Community.route) { CommunityScreen() }
            composable(AppRoute.Profile.route) { ProfileScreen() }
        }
    }
}