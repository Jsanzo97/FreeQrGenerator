package com.example.freeqrgenerator.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.options.QrOptions
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid

fun buildQrOptions(
    foregroundColor: Color,
    backgroundColor: Color,
    cornersRadius: Float,
    logoPainter: Painter? = null
) = QrOptions {
    scale = 0.9f
    background {
        fill = SolidColor(backgroundColor)
    }
    colors {
        dark = QrBrush.solid(foregroundColor)
        frame = QrBrush.solid(foregroundColor)
        ball = QrBrush.solid(foregroundColor)
    }
    shapes {
        darkPixel = QrPixelShape.roundCorners(cornersRadius)
        ball = QrBallShape.roundCorners(cornersRadius)
        frame = QrFrameShape.roundCorners(cornersRadius)
    }
    logoPainter?.let {
        logo {
            painter = it
            padding = QrLogoPadding.Natural(.1f)
            shape = QrLogoShape.circle()
            size = 0.25f
        }
    }
}