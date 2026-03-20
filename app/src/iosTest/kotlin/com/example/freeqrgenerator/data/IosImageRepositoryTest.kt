package com.example.freeqrgenerator.data

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class IosImageRepositoryTest {

    private val repository = IosImageRepository()

    @Test
    fun `Given valid image bytes When saveImage is called Then returns success`() = runTest {
        val validPngBytes = createValidPngBytes()

        val result = repository.saveImage(validPngBytes)

        result.isSuccess shouldBe true
    }

    @Test
    fun `Given corrupted image bytes When saveImage is called Then returns failure`() = runTest {
        val result = repository.saveImage(byteArrayOf(1, 2, 3))

        result.isFailure shouldBe true
    }

    private fun createValidPngBytes(): ByteArray {
        // Minimal valid 1x1 PNG
        return byteArrayOf(
            -119, 80, 78, 71, 13, 10, 26, 10, // PNG signature
            0, 0, 0, 13, 73, 72, 68, 82,       // IHDR chunk length + type
            0, 0, 0, 1, 0, 0, 0, 1,            // width=1 height=1
            8, 2, 0, 0, 0,                      // bit depth=8 color type=2
            -112, 119, 83, -34,                 // CRC
            0, 0, 0, 12, 73, 68, 65, 84,       // IDAT chunk
            8, -41, 99, -8, -49, -64, 0, 0,    // compressed data
            0, 2, 0, 1,                         // CRC
            -27, 39, -34, -4,                   // CRC
            0, 0, 0, 0, 73, 69, 78, 68,        // IEND chunk
            -82, 66, 96, -126                   // CRC
        )
    }
}