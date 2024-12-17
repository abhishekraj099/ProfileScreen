package com.example.profilescreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.util.Locale

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(
        AppRoute.Home,
        AppRoute.Explore,
        AppRoute.Community,
        AppRoute.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when(screen.route) {
                            AppRoute.Home.route -> Icons.Filled.Home
                            AppRoute.Explore.route -> Icons.Filled.Search
                            AppRoute.Community.route -> Icons.AutoMirrored.Filled.Chat
                            AppRoute.Profile.route -> Icons.Filled.Person
                            else -> Icons.Filled.Home
                        },
                        contentDescription = screen.route
                    )
                },
                label = { Text(screen.route.capitalize(Locale.ROOT)) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}