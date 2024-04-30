package com.example.freeqrgenerator.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFF6E00EE)
val PurpleGrey80 = Color(0xFF370083)
val Pink80 = Color(0xFF03DAC5)

val Purple40 = Color(0xFF6E00EE)
val PurpleGrey40 = Color(0xFF370083)
val Pink40 = Color(0xFF03DAC5)

val primary_dark = Purple80
val secondary_dark = PurpleGrey80
val tertiary_dark = Pink80

val primary_light = Purple40
val secondary_light = PurpleGrey40
val tertiary_light = Pink40


sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val text: Color
) {
    object Night: ThemeColors(
        background = Color.Black,
        surface = secondary_dark,
        primary = primary_dark,
        text = tertiary_dark
    )

    object Day: ThemeColors(
        background = Color.White,
        surface = secondary_light,
        primary = primary_light,
        text = tertiary_light
    )
}