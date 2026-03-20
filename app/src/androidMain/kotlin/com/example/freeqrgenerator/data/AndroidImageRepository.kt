package com.example.freeqrgenerator.data

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.freeqrgenerator.domain.repository.ImageRepository

class AndroidImageRepository(
    private val context: Context,
    private val contentResolver: ContentResolver = context.contentResolver
) : ImageRepository {

    override suspend fun saveImage(image: ByteArray): Result<Unit> {
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)

        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "QR_${System.currentTimeMillis()}.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FreeQr")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val uri: Uri? = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            if (uri != null) {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, contentValues, null, null)
                }
                Result.success(Unit)
            } else {
                return Result.failure(Exception("Failed to insert image"))
            }

        } catch (_: Exception) {
            Result.failure(Exception("Failed to generate QR code image"))
        }
    }
}
