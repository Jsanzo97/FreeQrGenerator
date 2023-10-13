package com.example.freeqrgenerator.ui.items

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.freeqrgenerator.MainState

@Composable
fun ChooseImageButton(modifier: Modifier, text: String, state: MainState) {
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        state.imageUri = uri
    }

    CustomButton(
        modifier = modifier,
        text = text
    ) {
        launcher.launch("image/*")
    }

    state.imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            state.bitmap = MediaStore.Images
                .Media.getBitmap(LocalContext.current.contentResolver,it)

        } else {
            val source = ImageDecoder
                .createSource(LocalContext.current.contentResolver,it)
            state.bitmap = ImageDecoder.decodeBitmap(source)
        }
    }
}