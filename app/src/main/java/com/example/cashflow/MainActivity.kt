package com.example.cashflow

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cashflow.navigation.MainNavigation
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.core.sensor.OrientationDetector
import com.example.cashflow.ui.core.sensor.ShakeDetector
import com.example.cashflow.ui.theme.CashFlowTheme
import com.example.cashflow.ui.global.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var orientationDetector: OrientationDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Shake detection → Logout
        shakeDetector = ShakeDetector(
            context = this,
            onShake = {
                viewModel.onShakeLogout()
            }
        )

        // Orientation → Black screen
        orientationDetector = OrientationDetector(
            context = this,
            onUpsideDown = {
                viewModel.setScreenBlack(true)
            },
            onUpright = {
                viewModel.setScreenBlack(false)
            }
        )

        val shakeEnabledRoutes = listOf(
            NavRoute.HomeScreen::class.qualifiedName,
            NavRoute.TransactionsScreen::class.qualifiedName,
            NavRoute.SaveTransactionScreen::class.qualifiedName
        )

        val blackScreenEnabledRoutes = listOf(
            NavRoute.HomeScreen::class.qualifiedName,
            NavRoute.TransactionsScreen::class.qualifiedName,
            NavRoute.SaveTransactionScreen::class.qualifiedName
        )

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val startDestination by viewModel.startDestination.collectAsState()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val isBlackScreen by viewModel.isBlackScreen.collectAsState()
            val context = LocalContext.current

            val alpha by animateFloatAsState(
                targetValue = if (isBlackScreen) 1f else 0f,
                label = "blackScreenAlpha"
            )

            LaunchedEffect(Unit) {
                viewModel.globalEvent.collect { event ->
                    when (event) {
                        is CommonUiEvent.LoggedOutByShake -> {
                            Toast.makeText(context, "Wylogowano przez potrząśnięcie", Toast.LENGTH_SHORT).show()
                            navController.navigate(NavRoute.LoginScreen) {
                                popUpTo(0)
                            }
                        }
                    }
                }
            }

            LaunchedEffect(currentRoute) {
                if (currentRoute in shakeEnabledRoutes) {
                    shakeDetector.start()
                } else {
                    shakeDetector.stop()
                }

                if (currentRoute in blackScreenEnabledRoutes) {
                    orientationDetector.start()
                } else {
                    orientationDetector.stop()
                }
            }

            CashFlowTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    MainNavigation(navController, startDestination)
                    if (alpha > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = alpha))
                                .zIndex(1000f)
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()
        orientationDetector.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
        orientationDetector.stop()
    }

}