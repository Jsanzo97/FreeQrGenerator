package com.example.freeqrgenerator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freeqrgenerator.splash.SplashScreen
import com.example.freeqrgenerator.ui.items.QrLayout
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme

@Composable
fun SetupNavGraph() {
    val navController = rememberNavController()
    FreeQrGeneratorTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
        ) {
            composable(route = Screen.Splash.route) {
                SplashScreen(
                    onAnimationFinished = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(route = Screen.Home.route) {
                QrLayout()
            }
        }
    }
}
