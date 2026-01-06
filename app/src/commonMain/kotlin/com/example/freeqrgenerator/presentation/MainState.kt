package com.example.freeqrgenerator.presentation

import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.error.FreeQrError

enum class ColorSelectorMode {
    NONE, FOREGROUND, BACKGROUND
}

data class MainState(
    val url: String = "",
    val shouldShowColorPicker: Boolean = false,
    val foregroundColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val selectorMode: ColorSelectorMode = ColorSelectorMode.NONE,
    val error: FreeQrError = FreeQrError.NONE,
    val isSaving: Boolean = false,
    val logoBytes: List<Byte>? = null,
    val qrCornersRadius: Float = 0.2f,
    val shouldShowCornersSlider: Boolean = false
)