package com.example.freeqrgenerator.domain.usecase

import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.domain.repository.QrRepository

interface GenerateQrUseCase {
    suspend operator fun invoke(
        url: String,
        foregroundColor: Color,
        backgroundColor: Color,
        cornersRadius: Float,
        logoBytes: List<Byte>?,
    ): Result<ByteArray>
}

class GenerateQrUseCaseImpl(
    val qrRepository: QrRepository,
) : GenerateQrUseCase {

    override suspend operator fun invoke(
        url: String,
        foregroundColor: Color,
        backgroundColor: Color,
        cornersRadius: Float,
        logoBytes: List<Byte>?,
    ) = qrRepository.generate(
        url = url,
        foregroundColor = foregroundColor,
        backgroundColor = backgroundColor,
        cornersRadius = cornersRadius,
        logoBytes = logoBytes,
    )
}
