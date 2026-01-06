package com.example.freeqrgenerator.data

import com.example.freeqrgenerator.domain.repository.PermissionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IosPermissionRepository : PermissionRepository {

    private val _permissionRequests = MutableSharedFlow<Unit>()
    override val permissionRequests: Flow<Unit> = _permissionRequests.asSharedFlow()

    override fun isWriteStorageGranted(): Boolean {
        val status = PHPhotoLibrary.authorizationStatus()
        return status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited
    }

    override suspend fun requestWriteStoragePermission() {
        suspendCoroutine { continuation ->
            PHPhotoLibrary.requestAuthorization { _ ->
                continuation.resume(Unit)
            }
        }
        _permissionRequests.emit(Unit)
    }
}