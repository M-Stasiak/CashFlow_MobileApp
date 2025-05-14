package com.example.cashflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cashflow.ui.screens.LoginScreen
import com.example.cashflow.ui.screens.RegisterScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoute.LoginScreen, builder = {
        composable<NavRoute.LoginScreen> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(NavRoute.RegisterScreen)
                }
            )
        }
        composable<NavRoute.RegisterScreen> {
            RegisterScreen()
        }
    })
}