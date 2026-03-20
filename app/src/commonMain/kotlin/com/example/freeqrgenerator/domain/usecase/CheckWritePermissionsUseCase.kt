package com.example.freeqrgenerator.domain.usecase

import com.example.freeqrgenerator.domain.repository.PermissionRepository

interface CheckWritePermissionsUseCase {
    operator fun invoke(): Boolean
}

class CheckWritePermissionsUseCaseImpl(
    val permissionRepository: PermissionRepository
): CheckWritePermissionsUseCase {

    override operator fun invoke() = permissionRepository.isWriteStorageGranted()

}

