package com.example.freeqrgenerator.ui.items

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.MainState

@Composable
fun QrLayout(state: MainState) {
    Column {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
        ) {
            QrPreview(state)
        }

        Row(
            modifier = Modifier
                .weight(3f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Bottom),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UrlInput(state)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        val launcher = rememberLauncherForActivityResult(contract =
                        ActivityResultContracts.GetContent()) { uri: Uri? ->
                            state.imageUri = uri
                        }

                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Choose Image"
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
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Generate Qr"
                        ) {
                            if (state.url.isNotEmpty()) {
                                state.qrGenerated = QrGenerator().processQr(state.url)
                            }
                        }
                    }
                }
            }
        }
    }
}