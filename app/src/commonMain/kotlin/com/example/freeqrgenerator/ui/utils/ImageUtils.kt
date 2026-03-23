package com.example.freeqrgenerator.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.decodeToImageBitmap
import kotlin.math.min

fun ByteArray.toCircularBitmapPainter(): BitmapPainter {
    val original = decodeToImageBitmap()
    val size = min(original.width, original.height)
    val result = ImageBitmap(size, size)
    val canvas = Canvas(result)

    val paint = Paint().apply {
        isAntiAlias = true
    }

    canvas.drawCircle(
        center = Offset(size / 2f, size / 2f),
        radius = size / 2f,
        paint = paint,
    )

    paint.blendMode = BlendMode.SrcIn
    canvas.drawImageRect(
        image = original,
        srcOffset = IntOffset(
            x = (original.width - size) / 2,
            y = (original.height - size) / 2,
        ),
        srcSize = IntSize(size, size),
        dstOffset = IntOffset.Zero,
        dstSize = IntSize(size, size),
        paint = paint,
    )

    return BitmapPainter(result)
}
