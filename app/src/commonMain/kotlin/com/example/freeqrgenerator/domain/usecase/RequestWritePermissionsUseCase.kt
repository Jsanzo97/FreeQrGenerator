package com.example.freeqrgenerator.domain.usecase

import com.example.freeqrgenerator.domain.repository.PermissionRepository
import kotlinx.coroutines.flow.Flow

interface RequestWritePermissionsUseCase {
    val requests: Flow<Unit>
    suspend operator fun invoke(): Result<Unit>
}

class RequestWritePermissionsUseCaseImpl(
    private val permissionRepository: PermissionRepository
): RequestWritePermissionsUseCase {

    override val requests: Flow<Unit> = permissionRepository.permissionRequests

    override suspend operator fun invoke() = permissionRepository.requestWriteStoragePermission()
}