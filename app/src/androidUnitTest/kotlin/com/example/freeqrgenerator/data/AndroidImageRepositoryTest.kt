package com.example.freeqrgenerator.data

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.ByteArrayOutputStream

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AndroidImageRepositoryTest {

    private val mockContext: Context = mockk(relaxed = true)
    private val mockContentResolver: ContentResolver = mockk(relaxed = true)
    private val mockUri: Uri = mockk(relaxed = true)

    private fun validImageBytes(): ByteArray {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private val repository = AndroidImageRepository(
        context = mockContext,
        contentResolver = mockContentResolver
    )

    @Test
    fun `Given valid image bytes and uri is returned When saveImage is called Then returns success`() = runTest {
        every { mockContentResolver.insert(any(), any()) } returns mockUri
        every { mockContentResolver.openOutputStream(mockUri) } returns ByteArrayOutputStream()

        val result = repository.saveImage(validImageBytes())

        result.isSuccess shouldBe true
    }

    @Test
    fun `Given uri is null When saveImage is called Then returns failure`() = runTest {
        every { mockContentResolver.insert(any(), any()) } returns null

        val result = repository.saveImage(validImageBytes())

        result.isFailure shouldBe true
    }

    @Test
    fun `Given contentResolver throws When saveImage is called Then returns failure`() = runTest {
        every { mockContentResolver.insert(any(), any()) } throws RuntimeException("ContentResolver error")

        val result = repository.saveImage(validImageBytes())

        result.isFailure shouldBe true
    }
}