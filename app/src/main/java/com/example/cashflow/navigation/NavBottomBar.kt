package com.example.cashflow.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavBottomBar(
    navController: NavHostController,
    items: List<NavItem>
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route::class.qualifiedName,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == navItem.route::class.qualifiedName) navItem.selectedIcon
                        else navItem.unselectedIcon,
                        contentDescription = navItem.title)
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}