package com.example.cashflow.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeMax
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cashflow.ui.screens.currency.CurrencyScreen
import com.example.cashflow.ui.screens.home.HomeScreen
import com.example.cashflow.ui.screens.login.LoginScreen
import com.example.cashflow.ui.screens.register.RegisterScreen
import com.example.cashflow.ui.screens.save_transaction.SaveTransactionScreen
import com.example.cashflow.ui.screens.transactions.TransactionsScreen

@Composable
fun MainNavigation(navController: NavHostController, startDestination: NavRoute) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Ekrany z wyświetlanym BottomBar
    val bottomBarRoutes = listOf(
        NavRoute.CurrencyScreen::class.qualifiedName,
        NavRoute.HomeScreen::class.qualifiedName,
        NavRoute.TransactionsScreen::class.qualifiedName
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
                            title = "Currency",
                            selectedIcon = Icons.Filled.CurrencyExchange,
                            unselectedIcon = Icons.Outlined.CurrencyExchange,
                            route = NavRoute.CurrencyScreen
                        ),
                        NavItem(
                            title = "Home",
                            selectedIcon = Icons.Default.Home,
                            unselectedIcon = Icons.Outlined.Home,
                            route = NavRoute.HomeScreen
                        ),
                        NavItem(
                            title = "Transactions",
                            selectedIcon = Icons.Filled.FilterList,
                            unselectedIcon = Icons.Outlined.FilterList,
                            route = NavRoute.TransactionsScreen
                        )
                    )
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = startDestination, modifier = Modifier.padding(it), builder = {
            composable<NavRoute.LoginScreen> {
                LoginScreen(navController = navController)
            }
            composable<NavRoute.RegisterScreen> {
                RegisterScreen(navController = navController)
            }
            composable<NavRoute.HomeScreen> {
                HomeScreen(navController = navController)
            }
            composable<NavRoute.TransactionsScreen> {
                TransactionsScreen(navController = navController)
            }
            composable<NavRoute.CurrencyScreen> {
                CurrencyScreen(navController = navController)
            }
            composable<NavRoute.SaveTransactionScreen> {
                SaveTransactionScreen(navController = navController)
            }
        })
    }
}