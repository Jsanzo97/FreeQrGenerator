package com.example.freeqrgenerator.data

import android.Manifest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
class AndroidPermissionRepositoryTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    private val repository = AndroidPermissionRepository(context)

    @Config(sdk = [29])
    @Test
    fun `Given SDK is Q or higher When isWriteStorageGranted is called Then returns true`() {
        repository.isWriteStorageGranted() shouldBe true
    }

    @Config(sdk = [28])
    @Test
    fun `Given SDK below Q and permission granted When isWriteStorageGranted is called Then returns true`() {
        Shadows.shadowOf(context as android.app.Application)
            .grantPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        repository.isWriteStorageGranted() shouldBe true
    }

    @Config(sdk = [28])
    @Test
    fun `Given SDK below Q and permission denied When isWriteStorageGranted is called Then returns false`() {
        repository.isWriteStorageGranted() shouldBe false
    }

    @Config(sdk = [29])
    @Test
    fun `Given SDK is Q or higher When requestWriteStoragePermission is called Then returns success and flow does not emit`() = runTest {
        val result = repository.requestWriteStoragePermission()

        result.isSuccess shouldBe true

        repository.permissionRequests.test {
            expectNoEvents()
        }
    }

    @Config(sdk = [28])
    @Test
    fun `Given SDK below Q When requestWriteStoragePermission is called Then returns success and flow emits`() = runTest {
        repository.permissionRequests.test {
            val result = repository.requestWriteStoragePermission()

            result.isSuccess shouldBe true
            awaitItem()
        }
    }
}