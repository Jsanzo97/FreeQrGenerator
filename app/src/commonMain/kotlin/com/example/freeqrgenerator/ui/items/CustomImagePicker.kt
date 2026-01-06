package com.example.freeqrgenerator.ui.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CustomImagePicker(
    onImageSelected: (ByteArray) -> Unit,
    modifier: Modifier = Modifier,
    text: String
)
