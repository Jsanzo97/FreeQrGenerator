package com.example.freeqrgenerator.presentation

import app.cash.turbine.test
import androidx.compose.ui.graphics.Color
import com.example.freeqrgenerator.domain.usecase.CheckWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.GenerateQrUseCase
import com.example.freeqrgenerator.domain.usecase.RequestWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.SaveImageUseCase
import com.example.freeqrgenerator.error.FreeQrError
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockedCheckWritePermissionsUseCase: CheckWritePermissionsUseCase = mock()
    private val mockedRequestWritePermissionsUseCase: RequestWritePermissionsUseCase = mock()
    private val mockedSaveImageUseCase: SaveImageUseCase = mock()
    private val mockedGenerateQrUseCase: GenerateQrUseCase = mock()

    private val permissionRequestsFlow = MutableSharedFlow<Unit>()

    private lateinit var viewModel: MainViewModel

    private val validUrl = "https://example.com"
    private val validBytes = ByteArray(10)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockedRequestWritePermissionsUseCase.requests } returns permissionRequestsFlow
        viewModel = MainViewModel(
            checkWritePermissionsUseCase = mockedCheckWritePermissionsUseCase,
            requestWritePermissionsUseCase = mockedRequestWritePermissionsUseCase,
            saveImageUseCase = mockedSaveImageUseCase,
            generateQrUseCase = mockedGenerateQrUseCase,
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given no action When uiState is observed Then initial state is default MainState`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()
        }
    }

    @Test
    fun `Given valid url When updateUrl is called Then url is updated and error is NONE`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.updateUrl(validUrl)

            awaitItem() shouldBe MainState(url = validUrl, error = FreeQrError.NONE)
        }
    }

    @Test
    fun `Given empty url When updateUrl is called Then error is URL_EMPTY`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.updateUrl("")

            awaitItem() shouldBe MainState(url = "", error = FreeQrError.URL_EMPTY)
        }
    }

    @Test
    fun `Given FOREGROUND mode When updateColorSelected is called Then foregroundColor is updated`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showColorPicker(ColorSelectorMode.FOREGROUND)
            awaitItem()

            viewModel.updateColorSelected(Color.Red)

            awaitItem() shouldBe MainState(
                foregroundColor = Color.Red,
                shouldShowColorPicker = true,
                selectorMode = ColorSelectorMode.FOREGROUND,
                error = FreeQrError.NONE
            )
        }
    }

    @Test
    fun `Given BACKGROUND mode When updateColorSelected is called Then backgroundColor is updated`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showColorPicker(ColorSelectorMode.BACKGROUND)
            awaitItem()

            viewModel.updateColorSelected(Color.Blue)

            awaitItem() shouldBe MainState(
                backgroundColor = Color.Blue,
                shouldShowColorPicker = true,
                selectorMode = ColorSelectorMode.BACKGROUND,
                error = FreeQrError.NONE
            )
        }
    }

    @Test
    fun `Given NONE mode When updateColorSelected is called Then state does not change`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.updateColorSelected(Color.Red)
            advanceUntilIdle()

            expectNoEvents()
        }
    }

    @Test
    fun `Given FOREGROUND mode When showColorPicker is called Then shouldShowColorPicker is true and selectorMode is FOREGROUND`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showColorPicker(ColorSelectorMode.FOREGROUND)

            awaitItem() shouldBe MainState(
                shouldShowColorPicker = true,
                selectorMode = ColorSelectorMode.FOREGROUND,
                shouldShowCornersSlider = false
            )
        }
    }

    @Test
    fun `Given corners slider visible When showColorPicker is called Then shouldShowCornersSlider is false`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showCornersSlider()
            awaitItem()

            viewModel.showColorPicker(ColorSelectorMode.FOREGROUND)

            awaitItem() shouldBe MainState(
                shouldShowColorPicker = true,
                selectorMode = ColorSelectorMode.FOREGROUND,
                shouldShowCornersSlider = false
            )
        }
    }

    @Test
    fun `Given color picker visible When hideColorPicker is called Then shouldShowColorPicker is false`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showColorPicker(ColorSelectorMode.FOREGROUND)
            awaitItem()

            viewModel.hideColorPicker()

            awaitItem() shouldBe MainState(
                shouldShowColorPicker = false,
                selectorMode = ColorSelectorMode.FOREGROUND
            )
        }
    }

    @Test
    fun `Given no action When showCornersSlider is called Then shouldShowCornersSlider is true`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showCornersSlider()

            awaitItem() shouldBe MainState(
                shouldShowCornersSlider = true,
                shouldShowColorPicker = false,
                selectorMode = ColorSelectorMode.NONE
            )
        }
    }

    @Test
    fun `Given color picker visible When showCornersSlider is called Then shouldShowColorPicker is false`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showColorPicker(ColorSelectorMode.FOREGROUND)
            awaitItem()

            viewModel.showCornersSlider()

            awaitItem() shouldBe MainState(
                shouldShowCornersSlider = true,
                shouldShowColorPicker = false,
                selectorMode = ColorSelectorMode.NONE
            )
        }
    }

    @Test
    fun `Given corners slider visible When hideCornersSlider is called Then shouldShowCornersSlider is false`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.showCornersSlider()
            awaitItem()

            viewModel.hideCornersSlider()

            awaitItem() shouldBe MainState(shouldShowCornersSlider = false)
        }
    }

    @Test
    fun `Given valid radius When updateCornersRadius is called Then qrCornersRadius is updated`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.updateCornersRadius(0.5f)

            awaitItem() shouldBe MainState(qrCornersRadius = 0.5f)
        }
    }

    @Test
    fun `Given image bytes When onImageSelected is called Then logoBytes is updated`() = runTest {
        val image = byteArrayOf(1, 2, 3)

        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.onImageSelected(image)

            awaitItem() shouldBe MainState(logoBytes = image.toList())
        }
    }

    @Test
    fun `Given empty url When onSaveImageClick is called Then error is URL_EMPTY and checkPermissions is not called`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe MainState()

            viewModel.onSaveImageClick()
            advanceUntilIdle()

            awaitItem() shouldBe MainState(error = FreeQrError.URL_EMPTY)

            verify(exactly(0)) { mockedCheckWritePermissionsUseCase.invoke() }
        }
    }

    @Test
    fun `Given permission not granted When onSaveImageClick is called Then requestPermissions is called and generateQr is not called`() = runTest {
        every { mockedCheckWritePermissionsUseCase.invoke() } returns false
        everySuspend { mockedRequestWritePermissionsUseCase.invoke() } returns Result.success(Unit)

        viewModel.updateUrl(validUrl)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onSaveImageClick()
            advanceUntilIdle()

            awaitItem() shouldBe MainState(url = validUrl, isSaving = true)
            awaitItem() shouldBe MainState(url = validUrl, isSaving = false)

            verify(exactly(1)) { mockedCheckWritePermissionsUseCase.invoke() }
            verifySuspend(exactly(1)) { mockedRequestWritePermissionsUseCase.invoke() }
            verifySuspend(exactly(0)) {
                mockedGenerateQrUseCase.invoke(
                    url = validUrl,
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    cornersRadius = 0.2f,
                    logoBytes = null
                )
            }
        }
    }

    @Test
    fun `Given permission granted and generate succeeds When onSaveImageClick is called Then snackbar event is emitted`() = runTest {
        every { mockedCheckWritePermissionsUseCase.invoke() } returns true
        everySuspend {
            mockedGenerateQrUseCase.invoke(
                url = validUrl,
                foregroundColor = Color.Black,
                backgroundColor = Color.White,
                cornersRadius = 0.2f,
                logoBytes = null
            )
        } returns Result.success(validBytes)
        everySuspend { mockedSaveImageUseCase.invoke(validBytes) } returns Result.success(Unit)

        viewModel.updateUrl(validUrl)

        viewModel.snackbarEvents.test {
            viewModel.onSaveImageClick()
            advanceUntilIdle()

            awaitItem()

            verify(exactly(1)) { mockedCheckWritePermissionsUseCase.invoke() }
            verifySuspend(exactly(1)) {
                mockedGenerateQrUseCase.invoke(
                    url = validUrl,
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    cornersRadius = 0.2f,
                    logoBytes = null
                )
            }
            verifySuspend(exactly(1)) { mockedSaveImageUseCase.invoke(validBytes) }
        }
    }

    @Test
    fun `Given permission granted and generate succeeds When onSaveImageClick is called Then isSaving goes true then false`() = runTest {
        every { mockedCheckWritePermissionsUseCase.invoke() } returns true
        everySuspend {
            mockedGenerateQrUseCase.invoke(
                url = validUrl,
                foregroundColor = Color.Black,
                backgroundColor = Color.White,
                cornersRadius = 0.2f,
                logoBytes = null
            )
        } returns Result.success(validBytes)
        everySuspend { mockedSaveImageUseCase.invoke(validBytes) } returns Result.success(Unit)

        viewModel.updateUrl(validUrl)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onSaveImageClick()
            advanceUntilIdle()

            awaitItem() shouldBe MainState(url = validUrl, isSaving = true)
            awaitItem() shouldBe MainState(url = validUrl, isSaving = false)

            verify(exactly(1)) { mockedCheckWritePermissionsUseCase.invoke() }
            verifySuspend(exactly(1)) {
                mockedGenerateQrUseCase.invoke(
                    url = validUrl,
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    cornersRadius = 0.2f,
                    logoBytes = null
                )
            }
            verifySuspend(exactly(1)) { mockedSaveImageUseCase.invoke(validBytes) }
        }
    }

    @Test
    fun `Given permission granted and generate fails When onSaveImageClick is called Then isSaving is false and saveImage is not called`() = runTest {
        every { mockedCheckWritePermissionsUseCase.invoke() } returns true
        everySuspend {
            mockedGenerateQrUseCase.invoke(
                url = validUrl,
                foregroundColor = Color.Black,
                backgroundColor = Color.White,
                cornersRadius = 0.2f,
                logoBytes = null
            )
        } returns Result.failure(Exception("Generate failed"))

        viewModel.updateUrl(validUrl)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onSaveImageClick()
            advanceUntilIdle()

            awaitItem() shouldBe MainState(url = validUrl, isSaving = true)
            awaitItem() shouldBe MainState(url = validUrl, isSaving = false)

            verify(exactly(1)) { mockedCheckWritePermissionsUseCase.invoke() }
            verifySuspend(exactly(1)) {
                mockedGenerateQrUseCase.invoke(
                    url = validUrl,
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    cornersRadius = 0.2f,
                    logoBytes = null
                )
            }
            verifySuspend(exactly(0)) { mockedSaveImageUseCase.invoke(validBytes) }
        }
    }

    @Test
    fun `Given permission granted and save fails When onSaveImageClick is called Then isSaving is false and no snackbar`() = runTest {
        every { mockedCheckWritePermissionsUseCase.invoke() } returns true
        everySuspend {
            mockedGenerateQrUseCase.invoke(
                url = validUrl,
                foregroundColor = Color.Black,
                backgroundColor = Color.White,
                cornersRadius = 0.2f,
                logoBytes = null
            )
        } returns Result.success(validBytes)
        everySuspend { mockedSaveImageUseCase.invoke(validBytes) } returns Result.failure(Exception("Save failed"))

        viewModel.updateUrl(validUrl)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onSaveImageClick()
            advanceUntilIdle()

            awaitItem() shouldBe MainState(url = validUrl, isSaving = true)
            awaitItem() shouldBe MainState(url = validUrl, isSaving = false)

            verify(exactly(1)) { mockedCheckWritePermissionsUseCase.invoke() }
            verifySuspend(exactly(1)) {
                mockedGenerateQrUseCase.invoke(
                    url = validUrl,
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    cornersRadius = 0.2f,
                    logoBytes = null
                )
            }
            verifySuspend(exactly(1)) { mockedSaveImageUseCase.invoke(validBytes) }
        }

        viewModel.snackbarEvents.test {
            expectNoEvents()
        }
    }

    @Test
    fun `Given generate throws exception When onSaveImageClick is called Then isSaving is false and saveImage is not called`() = runTest {
        every { mockedCheckWritePermissionsUseCase.invoke() } returns true
        everySuspend {
            mockedGenerateQrUseCase.invoke(
                url = validUrl,
                foregroundColor = Color.Black,
                backgroundColor = Color.White,
                cornersRadius = 0.2f,
                logoBytes = null
            )
        } throws Exception("Unexpected error")

        viewModel.updateUrl(validUrl)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onSaveImageClick()
            advanceUntilIdle()

            awaitItem() shouldBe MainState(url = validUrl, isSaving = true)
            awaitItem() shouldBe MainState(url = validUrl, isSaving = false)

            verify(exactly(1)) { mockedCheckWritePermissionsUseCase.invoke() }
            verifySuspend(exactly(0)) { mockedSaveImageUseCase.invoke(validBytes) }
        }
    }
}