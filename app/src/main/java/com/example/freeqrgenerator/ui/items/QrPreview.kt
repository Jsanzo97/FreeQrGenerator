package com.example.freeqrgenerator.ui.items

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.MainState
import com.google.accompanist.drawablepainter.rememberDrawablePainter

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
                    .background(Color.Transparent)
                    .wrapContentHeight(),
            )
        }
    }
}