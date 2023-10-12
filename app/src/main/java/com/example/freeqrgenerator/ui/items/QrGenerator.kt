package com.example.freeqrgenerator.ui.items

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.toArgb
import com.example.freeqrgenerator.MainState
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor

class QrGenerator {

    fun processQr(state: MainState): Drawable {
        val options = createQrVectorOptions {
            colors {
                dark = QrVectorColor.Solid(state.foregroundColor.toArgb())
                ball = QrVectorColor.Solid(state.foregroundColor.toArgb())
                frame = QrVectorColor.Solid(state.foregroundColor.toArgb())
            }
            background {
                color = QrVectorColor.Solid(state.backgroundColor.toArgb())
            }
        }

        return QrCodeDrawable(QrData.Url(state.url), options)
    }

}