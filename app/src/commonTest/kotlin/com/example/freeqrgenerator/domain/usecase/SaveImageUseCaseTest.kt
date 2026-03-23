package com.example.freeqrgenerator.domain.usecase

import com.example.freeqrgenerator.domain.repository.ImageRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveImageUseCaseTest {

    private val mockedImageRepository: ImageRepository = mock()
    private lateinit var useCase: SaveImageUseCase

    private val imageBytes = ByteArray(10)

    @BeforeTest
    fun setUp() {
        useCase = SaveImageUseCaseImpl(mockedImageRepository)
    }

    @Test
    fun `When useCase is invoked Then it should call repository saveImage`() = runTest {
        everySuspend {
            mockedImageRepository.saveImage(imageBytes)
        } returns Result.success(Unit)

        val result = useCase(imageBytes)

        result.isSuccess shouldBe true
    }
}
