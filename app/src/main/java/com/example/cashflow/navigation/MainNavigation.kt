package com.example.cashflow.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeMax
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cashflow.ui.screens.HomeScreen
import com.example.cashflow.ui.screens.LoginScreen
import com.example.cashflow.ui.screens.RegisterScreen

@Composable
fun MainNavigation(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Ekrany z wyświetlanym BottomBar
    val bottomBarRoutes = listOf(
        NavRoute.RegisterScreen::class.qualifiedName,
        NavRoute.HomeScreen::class.qualifiedName
    )
    val bottomBarVisibility = currentRoute in bottomBarRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(visible = bottomBarVisibility) {
                NavBottomBar(
                    navController = navController,
                    items = listOf(
                        NavItem(
                            title = "Logowanie",
                            selectedIcon = Icons.Default.Person,
                            unselectedIcon = Icons.Default.PersonOutline,
                            route = NavRoute.LoginScreen
                        ),
                        NavItem(
                            title = "Rejestracja",
                            selectedIcon = Icons.Default.Home,
                            unselectedIcon = Icons.Default.HomeMax,
                            route = NavRoute.RegisterScreen
                        ),
                        NavItem(
                            title = "Home",
                            selectedIcon = Icons.Default.Home,
                            unselectedIcon = Icons.Default.HomeMax,
                            route = NavRoute.HomeScreen
                        )
                    )
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = NavRoute.LoginScreen, modifier = Modifier.padding(it), builder = {
            composable<NavRoute.LoginScreen> {
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate(NavRoute.HomeScreen) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable<NavRoute.RegisterScreen> {
                RegisterScreen()
            }
            composable<NavRoute.HomeScreen> {
                HomeScreen()
            }
        })
    }
}