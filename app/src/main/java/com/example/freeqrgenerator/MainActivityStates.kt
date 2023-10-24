package com.example.freeqrgenerator

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.Window
import androidx.compose.ui.geometry.Rect
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
    val error: MainError = MainError.NONE,
    val qrBounds: Rect? = null,
    val qrWindow: Window? = null,
)
