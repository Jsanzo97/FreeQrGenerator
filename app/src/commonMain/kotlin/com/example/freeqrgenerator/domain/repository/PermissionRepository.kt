package com.example.freeqrgenerator.domain.repository

import kotlinx.coroutines.flow.Flow

interface PermissionRepository {
    val permissionRequests: Flow<Unit>
    fun isWriteStorageGranted(): Boolean
    suspend fun requestWriteStoragePermission()
}
