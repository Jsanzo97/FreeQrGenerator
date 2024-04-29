package com.example.freeqrgenerator.ui.items

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun QrView(
    qrGenerated: Drawable?,
    bitmapGenerated: Bitmap?,
    onBoundsCalculated: (bounds: Rect?) -> Unit
) {

    var bounds by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(bounds) {
        if (bounds != null) {
            onBoundsCalculated(bounds)
        }
    }

    Box(
        modifier = Modifier
            .border(
                width = if (qrGenerated == null) 1.dp else 0.dp,
                color = if (qrGenerated == null) Color.Black else Color.White,
            )
            .width(360.dp)
            .height(360.dp)
            .onGloballyPositioned {
                bounds = it.boundsInWindow()
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = qrGenerated),
            contentDescription = stringResource(id = R.string.qr_image_content_description),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight(),
        )

        bitmapGenerated?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = stringResource(id = R.string.qr_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(96.dp)
                    .height(96.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .wrapContentHeight(),
            )
        }
    }
}