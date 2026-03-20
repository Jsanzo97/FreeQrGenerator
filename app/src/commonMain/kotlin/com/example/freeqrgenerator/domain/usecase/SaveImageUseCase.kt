package com.example.freeqrgenerator.domain.usecase

import com.example.freeqrgenerator.domain.repository.ImageRepository

interface SaveImageUseCase {
    suspend operator fun invoke(image: ByteArray): Result<Unit>
}

class SaveImageUseCaseImpl(
    val imageRepository: ImageRepository
): SaveImageUseCase {

    override suspend operator fun invoke(image: ByteArray) = imageRepository.saveImage(image)

}