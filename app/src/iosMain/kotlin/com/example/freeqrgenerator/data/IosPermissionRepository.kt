package com.example.freeqrgenerator.data

import com.example.freeqrgenerator.domain.repository.PermissionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Photos.PHAccessLevelAddOnly
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import kotlin.coroutines.resume

class IosPermissionRepository(
    private val getAuthorizationStatus: () -> PHAuthorizationStatus = {
        PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelAddOnly)
    },
    private val requestAuthorization: ((PHAuthorizationStatus) -> Unit) -> Unit = { callback ->
        PHPhotoLibrary.requestAuthorizationForAccessLevel(PHAccessLevelAddOnly, callback)
    },
) : PermissionRepository {

    override val permissionRequests: Flow<Unit> = emptyFlow()

    override fun isWriteStorageGranted(): Boolean {
        val status = getAuthorizationStatus()
        return status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited
    }

    override suspend fun requestWriteStoragePermission(): Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            requestAuthorization { status ->
                if (status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited) {
                    continuation.resume(Result.success(Unit))
                } else {
                    continuation.resume(Result.failure(Exception("Photo library permission denied")))
                }
            }
        }
    }
}
