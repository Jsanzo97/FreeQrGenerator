package com.example.freeqrgenerator.data

import com.example.freeqrgenerator.domain.repository.ImageRepository
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import kotlin.coroutines.resume

class IosImageRepository : ImageRepository {

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun saveImage(image: ByteArray): Result<Unit> {
        return try {
            val nsData = image.usePinned { pinned ->
                NSData.dataWithBytes(pinned.addressOf(0), image.size.toULong())
            }

            val uiImage = UIImage(data = nsData)

            suspendCancellableCoroutine { continuation ->
                UIImageWriteToSavedPhotosAlbum(
                    image = uiImage,
                    completionTarget = null,
                    completionSelector = null,
                    contextInfo = null
                )
                continuation.resume(Result.success(Unit))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}