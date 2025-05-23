package com.example.cashflow.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: NavRoute
)