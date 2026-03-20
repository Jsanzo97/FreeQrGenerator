package com.example.freeqrgenerator.domain.usecase

import com.example.freeqrgenerator.domain.repository.PermissionRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class CheckWritePermissionsUseCaseTest {

    private val mockedPermissionRepository: PermissionRepository = mock()
    private lateinit var useCase: CheckWritePermissionsUseCase

    @BeforeTest
    fun setUp() {
        useCase = CheckWritePermissionsUseCaseImpl(mockedPermissionRepository)
    }

    @Test
    fun `Given permission is granted When useCase is invoked Then it should return true`() {
        every { mockedPermissionRepository.isWriteStorageGranted() } returns true

        val result = useCase()

        result shouldBe true
    }

    @Test
    fun `Given permission is NOT granted When useCase is invoked Then it should return false`() {
        every { mockedPermissionRepository.isWriteStorageGranted() } returns false

        val result = useCase()

        result shouldBe false
    }
}
