package com.example.freeqrgenerator

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.ui.items.ColorSelector

class MainState {

    var url: String by mutableStateOf("")
    var qrGenerated: Drawable? by mutableStateOf(null)
    var imageUri: Uri? by mutableStateOf(null)
    var bitmap: Bitmap? by mutableStateOf(null)
    var showColorPicker: Boolean by mutableStateOf(false)
    var foregroundColor: Color by mutableStateOf(Color.Black)
    var backgroundColor: Color by mutableStateOf(Color.White)
    var selectorMode: ColorSelector by mutableStateOf(ColorSelector.NONE)

}