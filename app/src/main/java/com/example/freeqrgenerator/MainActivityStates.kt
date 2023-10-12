package com.example.freeqrgenerator

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainState {

    var url: String by mutableStateOf("")
    var qrGenerated: Drawable? by mutableStateOf(null)
    var imageUri: Uri? by mutableStateOf(null)
    var bitmap: Bitmap? by mutableStateOf(null)
}