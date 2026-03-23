package com.example.freeqrgenerator.ui.items

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import java.io.ByteArrayOutputStream

@Composable
actual fun CustomImagePicker(
    onImageSelected: (ByteArray) -> Unit,
    modifier: Modifier,
    text: String,
) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            onImageSelected(byteArray)
        }
    }

    CustomButton(
        modifier = modifier,
        text = text,
        icon = Icons.Default.Image,
        isCircular = true,
        onClick = {
            launcher.launch("image/*")
        },
    )
}

@Preview
@Composable
private fun CustomImagePickerPreview() {
    MaterialTheme {
        CustomImagePicker(
            onImageSelected = {},
            text = "Seleccionar imagen",
        )
    }
}
