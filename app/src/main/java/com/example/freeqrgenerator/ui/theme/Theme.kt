package com.example.freeqrgenerator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class FreeQrGeneratorColors(
    val primary: Color,
    val secondary: Color,
    val surface: Color,
    val background: Color,
    val opposite: Color,
    val error: Color,
)

val lightColors =
    FreeQrGeneratorColors(
        primary = primary_light,
        secondary = secondary_light,
        surface = tertiary_light,
        background = Color.White,
        opposite = Color.Black,
        error = Color.Red,
    )

val darkColors =
    FreeQrGeneratorColors(
        primary = primary_dark,
        secondary = secondary_dark,
        surface = tertiary_dark,
        background = Color.Black,
        opposite = Color.White,
        error = Color.Red,
    )

val LocalLightColors = staticCompositionLocalOf { lightColors }
val LocalDarkColors = staticCompositionLocalOf { darkColors }

object FreeQrGeneratorTheme {
    val colors: FreeQrGeneratorColors
    @Composable
    get() = if (isSystemInDarkTheme()) LocalDarkColors.current else LocalLightColors.current
}

object FreeQrGeneratorColorProvider {
    val colors: ProvidableCompositionLocal<FreeQrGeneratorColors>
    @Composable
    get() = if (isSystemInDarkTheme()) LocalDarkColors else LocalLightColors
}

@Composable
fun FreeQrGeneratorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit = {}
) {

    val colors = if (darkTheme) {
        darkColors
    } else {
        lightColors
    }

    CompositionLocalProvider(
        value = FreeQrGeneratorColorProvider.colors provides colors,
        content = content
    )
}