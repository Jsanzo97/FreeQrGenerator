package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.presentation.buildQrOptions
import com.example.freeqrgenerator.resources.Res
import com.example.freeqrgenerator.resources.qr_image_content_description
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import com.example.freeqrgenerator.ui.utils.toCircularBitmapPainter
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.stringResource

@Composable
fun QrViewContent(
    url: String,
    foregroundColor: Color,
    backgroundColor: Color,
    qrCornersRadius: Float,
    logoBytes: List<Byte>?,
    modifier: Modifier = Modifier,
) {
    val logoPainter = remember(logoBytes) {
        logoBytes?.toByteArray()?.toCircularBitmapPainter()
    }

    val qrOptions = remember(foregroundColor, backgroundColor, qrCornersRadius, logoPainter) {
        buildQrOptions(
            foregroundColor = foregroundColor,
            backgroundColor = backgroundColor,
            cornersRadius = qrCornersRadius,
            logoPainter = logoPainter
        )
    }

    val qrPainter = rememberQrCodePainter(url, qrOptions)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (url.isNotEmpty()) {
                Image(
                    painter = qrPainter,
                    contentDescription = stringResource(Res.string.qr_image_content_description),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Preview
@Composable
private fun QrViewPreview() {
    FreeQrGeneratorTheme {
        QrViewContent(
            url = "https://example.com",
            foregroundColor = Color.Black,
            backgroundColor = Color.White,
            qrCornersRadius = 0.2f,
            logoBytes = null,
        )
    }
}