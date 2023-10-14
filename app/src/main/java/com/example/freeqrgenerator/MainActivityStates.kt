package com.example.freeqrgenerator

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.ui.items.ColorSelector

data class MainState (
    val url: String = "",
    val qrGenerated: Drawable? = null,
    val bitmap: Bitmap? = null,
    val shouldShowColorPicker: Boolean = false,
    val foregroundColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val selectorMode: ColorSelector = ColorSelector.NONE,
    val error: MainError = MainError.NONE
)
