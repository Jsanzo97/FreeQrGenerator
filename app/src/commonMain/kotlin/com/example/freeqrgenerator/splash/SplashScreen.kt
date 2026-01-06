package com.example.freeqrgenerator.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.freeqrgenerator.navigation.Screen
import com.example.freeqrgenerator.resources.Res
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(
    onAnimationFinished: () -> Unit
) {
    
    val composition by rememberLottieComposition {
        val json = Res.readBytes("files/splash_logo_animation.json").decodeToString()
        LottieCompositionSpec.JsonString(json)
    }
    
    val logoAnimationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = true
    )

    LaunchedEffect(logoAnimationState.isAtEnd, composition) {
        if (logoAnimationState.isAtEnd && composition != null) {
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        composition?.let { comp ->
            Image(
                painter = rememberLottiePainter(
                    composition = comp,
                    progress = { logoAnimationState.progress }
                ),
                contentDescription = "Lottie animation",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
