package com.example.freeqrgenerator.data

import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.domain.repository.QrRepository
import com.example.freeqrgenerator.presentation.buildQrOptions
import com.example.freeqrgenerator.ui.utils.toCircularBitmapPainter
import io.github.alexzhirkevich.qrose.ImageFormat
import io.github.alexzhirkevich.qrose.QrCodePainter
import io.github.alexzhirkevich.qrose.toByteArray

class QrRepositoryImpl : QrRepository {

    override suspend fun generate(
        url: String,
        foregroundColor: Color,
        backgroundColor: Color,
        cornersRadius: Float,
        logoBytes: List<Byte>?,
    ): Result<ByteArray> {
        return try {
            val logoPainter = logoBytes?.toByteArray()?.toCircularBitmapPainter()

            val painter = QrCodePainter(
                data = url,
                options = buildQrOptions(
                    foregroundColor = foregroundColor,
                    backgroundColor = backgroundColor,
                    cornersRadius = cornersRadius,
                    logoPainter = logoPainter,
                ),
            )

            Result.success(painter.toByteArray(1024, 1024, ImageFormat.PNG))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
