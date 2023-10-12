package com.example.freeqrgenerator

import android.content.res.Resources
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.google.accompanist.drawablepainter.rememberDrawablePainter


class MainActivity : ComponentActivity() {

    private var state = MainState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeQrGeneratorTheme {
                QrLayout(state)
            }
        }
    }
}

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
                        ChooseForeground(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            state
                        )
                        UploadImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            state
                        )
                    }
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        ChooseBackground(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            state
                        )
                        GenerateQrButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            state
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QrPreview(state: MainState) {
    Box(
        modifier = Modifier
            .border(
                width = if (state.qrGenerated == null) 1.dp else 0.dp,
                color = if (state.qrGenerated == null) Color.Black else Color.White,
            )
            .width(360.dp)
            .height(360.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = state.qrGenerated),
            contentDescription = "Qr generated",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight(),
        )

        state.bitmap?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = "Qr generated",
                modifier = Modifier
                    .width(96.dp)
                    .height(96.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .wrapContentHeight(),
            )
        }
    }
}

@Composable
fun UrlInput(state: MainState) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.url,
        onValueChange = { state.url = it},
        label = { Text("Introduce your url")}
    )
}

@Composable
fun GenerateQrButton(modifier: Modifier, state: MainState) {
    val resources = LocalContext.current.resources
    Button(
        shape = RoundedCornerShape(10),
        onClick = {
          if (state.url.isNotEmpty()) {
              state.qrGenerated = processQr(state.url, resources)
          }
        },
        modifier = modifier
    ) {
        Text(
            text = "Generate Qr",
        )
    }
}

fun processQr(url: String, resources: Resources): Drawable {
    val options = QrVectorOptions.Builder()

    return QrCodeDrawable(QrData.Url(url), options.build())
}

@Composable
fun UploadImage(modifier: Modifier, state: MainState) {
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        state.imageUri = uri
    }

    Button(
        shape = RoundedCornerShape(10),
        onClick = { launcher.launch("image/*") },
        modifier = modifier
    ) {
        Text(
            text = "Choose image",
        )
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

@Composable
fun ChooseForeground(modifier: Modifier, state: MainState) {
    val resources = LocalContext.current.resources
    Button(
        shape = RoundedCornerShape(10),
        onClick = {
            if (state.url.isNotEmpty()) {
                state.qrGenerated = processQr(state.url, resources)
            }
        },
        modifier = modifier
    ) {
        Text(
            text = "Main color",
        )
    }
}

@Composable
fun ChooseBackground(modifier: Modifier, state: MainState) {
    val resources = LocalContext.current.resources
    Button(
        shape = RoundedCornerShape(10),
        onClick = {
            if (state.url.isNotEmpty()) {
                state.qrGenerated = processQr(state.url, resources)
            }
        },
        modifier = modifier
    ) {
        Text(
            text = "Background",
        )
    }
}

@Preview(showBackground = true, heightDp = 1280, widthDp = 720)
@Composable
fun Preview() {
    FreeQrGeneratorTheme {
        QrLayout(MainState())
    }
}