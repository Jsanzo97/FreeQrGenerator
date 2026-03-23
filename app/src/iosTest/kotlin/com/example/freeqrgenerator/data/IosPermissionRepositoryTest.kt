package com.example.freeqrgenerator.data

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import kotlin.test.Test

class IosPermissionRepositoryTest {

    @Test
    fun `Given status is Authorized When isWriteStorageGranted is called Then returns true`() {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusAuthorized },
        )

        repository.isWriteStorageGranted() shouldBe true
    }

    @Test
    fun `Given status is Limited When isWriteStorageGranted is called Then returns true`() {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusLimited },
        )

        repository.isWriteStorageGranted() shouldBe true
    }

    @Test
    fun `Given status is Denied When isWriteStorageGranted is called Then returns false`() {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusDenied },
        )

        repository.isWriteStorageGranted() shouldBe false
    }

    @Test
    fun `Given status is NotDetermined When isWriteStorageGranted is called Then returns false`() {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusNotDetermined },
        )

        repository.isWriteStorageGranted() shouldBe false
    }

    @Test
    fun `Given authorization returns Authorized When requestWriteStoragePermission is called Then returns success`() = runTest {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusAuthorized },
            requestAuthorization = { callback -> callback(PHAuthorizationStatusAuthorized) },
        )

        val result = repository.requestWriteStoragePermission()

        result.isSuccess shouldBe true
    }

    @Test
    fun `Given authorization returns Limited When requestWriteStoragePermission is called Then returns success`() = runTest {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusLimited },
            requestAuthorization = { callback -> callback(PHAuthorizationStatusLimited) },
        )

        val result = repository.requestWriteStoragePermission()

        result.isSuccess shouldBe true
    }

    @Test
    fun `Given authorization returns Denied When requestWriteStoragePermission is called Then returns failure`() = runTest {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusDenied },
            requestAuthorization = { callback -> callback(PHAuthorizationStatusDenied) },
        )

        val result = repository.requestWriteStoragePermission()

        result.isFailure shouldBe true
    }

    @Test
    fun `Given authorization returns NotDetermined When requestWriteStoragePermission is called Then returns failure`() = runTest {
        val repository = IosPermissionRepository(
            getAuthorizationStatus = { PHAuthorizationStatusNotDetermined },
            requestAuthorization = { callback -> callback(PHAuthorizationStatusNotDetermined) },
        )

        val result = repository.requestWriteStoragePermission()

        result.isFailure shouldBe true
    }
}
