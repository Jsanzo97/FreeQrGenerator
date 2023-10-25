package com.example.freeqrgenerator

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.ui.items.ColorSelector
import com.example.freeqrgenerator.ui.utils.Constants

data class MainState (
    val url: String = Constants.STRING_EMPTY,
    val qrGenerated: Drawable? = null,
    val bitmap: Bitmap? = null,
    val shouldShowColorPicker: Boolean = false,
    val foregroundColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val selectorMode: ColorSelector = ColorSelector.NONE,
    val error: MainError = MainError.NONE
)

enum class MainError {
    URL_EMPTY, NONE
}
