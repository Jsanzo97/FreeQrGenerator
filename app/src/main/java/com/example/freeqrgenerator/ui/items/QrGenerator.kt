package com.example.freeqrgenerator.ui.items

import android.graphics.drawable.Drawable
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions

class QrGenerator {

    fun processQr(url: String): Drawable {
        val options = QrVectorOptions.Builder()

        return QrCodeDrawable(QrData.Url(url), options.build())
    }

}