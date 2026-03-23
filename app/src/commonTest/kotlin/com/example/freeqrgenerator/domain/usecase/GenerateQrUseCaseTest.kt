package com.example.freeqrgenerator.domain.usecase

import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.domain.repository.QrRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateQrUseCaseTest {

    private val mockedQrRepository: QrRepository = mock()
    private lateinit var useCase: GenerateQrUseCase

    private val validUrl = "https://example.com"
    private val foregroundColor = Color.Black
    private val backgroundColor = Color.White
    private val cornersRadius = 0.2f
    private val resultBytes = ByteArray(10)

    @BeforeTest
    fun setUp() {
        useCase = GenerateQrUseCaseImpl(mockedQrRepository)
    }

    @Test
    fun `When useCase is invoked Then it should call repository generate`() = runTest {
        everySuspend {
            mockedQrRepository.generate(
                url = validUrl,
                foregroundColor = foregroundColor,
                backgroundColor = backgroundColor,
                cornersRadius = cornersRadius,
                logoBytes = null,
            )
        } returns Result.success(resultBytes)

        val result = useCase(
            url = validUrl,
            foregroundColor = foregroundColor,
            backgroundColor = backgroundColor,
            cornersRadius = cornersRadius,
            logoBytes = null,
        )

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe resultBytes
    }
}
