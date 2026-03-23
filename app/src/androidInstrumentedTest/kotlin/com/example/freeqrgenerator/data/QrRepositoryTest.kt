package com.example.freeqrgenerator.data

import androidx.compose.ui.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QrRepositoryTest {

    private val repository = QrRepositoryImpl()

    @Test
    fun givenValidUrl_whenGenerateIsCalled_thenReturnsSuccessWithNonEmptyBytes() = runTest {
        val result = repository.generate(
            url = "https://example.com",
            foregroundColor = Color.Black,
            backgroundColor = Color.White,
            cornersRadius = 0.2f,
            logoBytes = null
        )

        result.isSuccess shouldBe true
        result.getOrNull()?.isNotEmpty() shouldBe true
    }

    @Test
    fun givenEmptyUrl_whenGenerateIsCalled_thenReturnsSuccess() = runTest {
        val result = repository.generate(
            url = "",
            foregroundColor = Color.Black,
            backgroundColor = Color.White,
            cornersRadius = 0.2f,
            logoBytes = null
        )

        result.isSuccess shouldBe true
    }

    @Test
    fun givenCorruptedLogoBytes_whenGenerateIsCalled_thenReturnsFailure() = runTest {
        val result = repository.generate(
            url = "https://example.com",
            foregroundColor = Color.Black,
            backgroundColor = Color.White,
            cornersRadius = 0.2f,
            logoBytes = listOf(1, 2)
        )

        result.isFailure shouldBe true
    }
}