package com.example.freeqrgenerator.domain.repository

interface ImageRepository {
    suspend fun saveImage(image: ByteArray): Result<Unit>
}
