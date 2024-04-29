package com.example.freeqrgenerator.ui.items

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.freeqrgenerator.ui.utils.Constants

@Composable
fun ChooseImageButton(
    modifier: Modifier,
    text: String,
    onImageSelected: (uri: Uri?) -> Unit
) {
    var uri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) {
        uri = it
        onImageSelected(uri)
    }

    CustomButton(
        modifier = modifier,
        text = text
    ) {
        launcher.launch(Constants.IMAGE_LAUNCHER)
    }
}
