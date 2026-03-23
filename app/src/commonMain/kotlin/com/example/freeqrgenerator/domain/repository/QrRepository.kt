package com.example.freeqrgenerator.domain.repository

import androidx.compose.ui.graphics.Color

interface QrRepository {
    suspend fun generate(
        url: String,
        foregroundColor: Color,
        backgroundColor: Color,
        cornersRadius: Float,
        logoBytes: List<Byte>?,
    ): Result<ByteArray>
}
