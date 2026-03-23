package com.example.freeqrgenerator.android

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.freeqrgenerator.domain.usecase.CheckWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.GenerateQrUseCase
import com.example.freeqrgenerator.domain.usecase.RequestWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.SaveImageUseCase
import com.example.freeqrgenerator.presentation.MainViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val mockCheckWritePermissionsUseCase: CheckWritePermissionsUseCase = mockk()
    private val mockRequestWritePermissionsUseCase: RequestWritePermissionsUseCase = mockk()

    private val fakeGenerateQrUseCase = object : GenerateQrUseCase {
        override suspend fun invoke(
            url: String,
            foregroundColor: Color,
            backgroundColor: Color,
            cornersRadius: Float,
            logoBytes: List<Byte>?
        ): Result<ByteArray> = Result.success(ByteArray(10))
    }

    private val fakeSaveImageUseCase = object : SaveImageUseCase {
        override suspend fun invoke(image: ByteArray): Result<Unit> = Result.success(Unit)
    }

    private lateinit var testModule: Module

    @get:Rule(order = 0)
    val koinRule = object : ExternalResource() {
        override fun before() {
            every { mockRequestWritePermissionsUseCase.requests } returns emptyFlow()
            testModule = module {
                single<MainViewModel> {
                    MainViewModel(
                        checkWritePermissionsUseCase = mockCheckWritePermissionsUseCase,
                        requestWritePermissionsUseCase = mockRequestWritePermissionsUseCase,
                        saveImageUseCase = fakeSaveImageUseCase,
                        generateQrUseCase = fakeGenerateQrUseCase,
                    )
                }
            }
            loadKoinModules(testModule)
        }

        override fun after() {
            unloadKoinModules(testModule)
        }
    }

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun givenAppLaunches_whenMainScreenShown_thenMainScreenIsDisplayed() {
        composeTestRule
            .onNodeWithTag("main_screen")
            .assertIsDisplayed()
    }

    @Test
    fun givenAppLaunches_whenMainScreenShown_thenUrlInputIsDisplayed() {
        composeTestRule
            .onNodeWithTag("url_input")
            .assertIsDisplayed()
    }

    @Test
    fun givenAppLaunches_whenMainScreenShown_thenSaveButtonIsDisplayed() {
        composeTestRule
            .onNodeWithText("Save Qr")
            .assertIsDisplayed()
    }

    @Test
    fun givenUrlInputVisible_whenUserTypesUrl_thenUrlIsShownInInput() {
        composeTestRule
            .onNodeWithTag("url_input")
            .performTextInput("https://example.com")

        composeTestRule
            .onNodeWithText("https://example.com")
            .assertIsDisplayed()
    }

    @Test
    fun givenSaveButtonVisible_whenClickedWithEmptyUrl_thenErrorIconIsShown() {
        composeTestRule
            .onNodeWithTag("save_button")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Error")
            .assertIsDisplayed()
    }

    @Test
    fun givenValidUrl_whenSaveButtonClicked_thenSnackbarIsShown() {
        every { mockCheckWritePermissionsUseCase.invoke() } returns true

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("url_input")
            .performTextInput("https://example.com")

        composeTestRule.activityRule.scenario.onActivity { activity ->
            val imm = activity.getSystemService(android.view.inputmethod.InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("save_button")
            .assertIsEnabled()
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            try {
                composeTestRule
                    .onNodeWithText("Image saved")
                    .assertIsDisplayed()
                true
            } catch (_: AssertionError) {
                false
            }
        }

        composeTestRule
            .onNodeWithText("Image saved")
            .assertIsDisplayed()
    }
}