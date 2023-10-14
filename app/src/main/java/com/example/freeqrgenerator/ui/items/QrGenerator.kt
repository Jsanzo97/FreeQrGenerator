package com.example.freeqrgenerator.ui.items

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor

class QrGenerator {

    fun processQr(foregroundColor: Color, backgroundColor: Color, url: String): Drawable {
        val options = createQrVectorOptions {
            colors {
                dark = QrVectorColor.Solid(foregroundColor.toArgb())
                ball = QrVectorColor.Solid(foregroundColor.toArgb())
                frame = QrVectorColor.Solid(foregroundColor.toArgb())
            }
            background {
                color = QrVectorColor.Solid(backgroundColor.toArgb())
            }
        }

        return QrCodeDrawable(QrData.Url(url), options)
    }

}