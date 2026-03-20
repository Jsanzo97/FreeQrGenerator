package com.example.freeqrgenerator.domain.usecase

import com.example.freeqrgenerator.domain.repository.PermissionRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestWritePermissionsUseCaseTest {

    private val mockedPermissionRepository: PermissionRepository = mock()
    private lateinit var useCase: RequestWritePermissionsUseCase
    private val permissionRequestsFlow = MutableSharedFlow<Unit>()

    @BeforeTest
    fun setUp() {
        // Se debe mockear antes de crear la instancia porque se accede en el constructor/init
        every { mockedPermissionRepository.permissionRequests } returns permissionRequestsFlow
        useCase = RequestWritePermissionsUseCaseImpl(mockedPermissionRepository)
    }

    @Test
    fun `When useCase is invoked Then it should call repository requestWriteStoragePermission`() = runTest {
        everySuspend {
            mockedPermissionRepository.requestWriteStoragePermission()
        } returns Result.success(Unit)

        val result = useCase()

        result.isSuccess shouldBe true
    }

    @Test
    fun `When requests is accessed Then it should return the flow from repository`() {
        useCase.requests shouldBe permissionRequestsFlow
    }
}
