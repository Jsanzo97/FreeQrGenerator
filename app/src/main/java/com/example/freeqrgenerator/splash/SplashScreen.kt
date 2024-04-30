package com.example.freeqrgenerator.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.freeqrgenerator.R
import com.example.freeqrgenerator.navigation.Screen
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FreeQrGeneratorTheme.colors.background)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_logo_animation))
        val logoAnimationState = animateLottieCompositionAsState(composition = composition)

        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
            navController.navigate(Screen.Home.route)
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { logoAnimationState.progress }
            )

            Text(
                text = "Free Qr Generator",
                fontSize = 24.sp,
                color = FreeQrGeneratorTheme.colors.opposite
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        NavHostController(LocalContext.current)
    )
}