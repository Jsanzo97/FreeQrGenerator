package com.example.freeqrgenerator.ui.items

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.freeqrgenerator.error.FreeQrError
import com.example.freeqrgenerator.presentation.ColorSelectorMode
import com.example.freeqrgenerator.presentation.QrLayoutCallBacks
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [33],
    qualifiers = "xxhdpi",
    manifest = Config.NONE,
)
class QrLayoutScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val noOpCallbacks = object : QrLayoutCallBacks {
        override fun onUrlUpdated(url: String) {}
        override fun onShowColorPicker(colorSelectorMode: ColorSelectorMode) {}
        override fun onImageSelected(image: ByteArray) {}
        override fun onSaveImageClick() {}
        override fun onColorPickerDismiss() {}
        override fun onColorSelected(color: Color) {}
        override fun onShowCornersSlider() {}
        override fun onCornersSliderDismiss() {}
        override fun onCornersRadiusChanged(radius: Float) {}
    }

    @Test
    fun qrLayout_defaultState() {
        composeTestRule.setContent {
            FreeQrGeneratorTheme {
                QrLayoutContent(
                    url = "",
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    selectorMode = ColorSelectorMode.NONE,
                    shouldShowColorPicker = false,
                    shouldShowCornersSlider = false,
                    qrCornersRadius = 0.2f,
                    logoBytes = null,
                    isSaving = false,
                    error = FreeQrError.NONE,
                    snackbarHostState = SnackbarHostState(),
                    callBacks = noOpCallbacks,
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun qrLayout_withUrl() {
        composeTestRule.setContent {
            FreeQrGeneratorTheme {
                QrLayoutContent(
                    url = "https://example.com",
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    selectorMode = ColorSelectorMode.NONE,
                    shouldShowColorPicker = false,
                    shouldShowCornersSlider = false,
                    qrCornersRadius = 0.2f,
                    logoBytes = null,
                    isSaving = false,
                    error = FreeQrError.NONE,
                    snackbarHostState = SnackbarHostState(),
                    callBacks = noOpCallbacks,
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun qrLayout_errorState() {
        composeTestRule.setContent {
            FreeQrGeneratorTheme {
                QrLayoutContent(
                    url = "",
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    selectorMode = ColorSelectorMode.NONE,
                    shouldShowColorPicker = false,
                    shouldShowCornersSlider = false,
                    qrCornersRadius = 0.2f,
                    logoBytes = null,
                    isSaving = false,
                    error = FreeQrError.URL_EMPTY,
                    snackbarHostState = SnackbarHostState(),
                    callBacks = noOpCallbacks,
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun qrLayout_savingState() {
        composeTestRule.setContent {
            FreeQrGeneratorTheme {
                QrLayoutContent(
                    url = "https://example.com",
                    foregroundColor = Color.Black,
                    backgroundColor = Color.White,
                    selectorMode = ColorSelectorMode.NONE,
                    shouldShowColorPicker = false,
                    shouldShowCornersSlider = false,
                    qrCornersRadius = 0.2f,
                    logoBytes = null,
                    isSaving = true,
                    error = FreeQrError.NONE,
                    snackbarHostState = SnackbarHostState(),
                    callBacks = noOpCallbacks,
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun qrLayout_customColors() {
        composeTestRule.setContent {
            FreeQrGeneratorTheme {
                QrLayoutContent(
                    url = "https://example.com",
                    foregroundColor = Color(0xFF6200EE),
                    backgroundColor = Color(0xFFFFF9C4),
                    selectorMode = ColorSelectorMode.NONE,
                    shouldShowColorPicker = false,
                    shouldShowCornersSlider = false,
                    qrCornersRadius = 0.5f,
                    logoBytes = null,
                    isSaving = false,
                    error = FreeQrError.NONE,
                    snackbarHostState = SnackbarHostState(),
                    callBacks = noOpCallbacks,
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun qrLayout_darkTheme() {
        composeTestRule.setContent {
            FreeQrGeneratorTheme(darkTheme = true) {
                QrLayoutContent(
                    url = "https://example.com",
                    foregroundColor = Color.White,
                    backgroundColor = Color.Black,
                    selectorMode = ColorSelectorMode.NONE,
                    shouldShowColorPicker = false,
                    shouldShowCornersSlider = false,
                    qrCornersRadius = 0.2f,
                    logoBytes = null,
                    isSaving = false,
                    error = FreeQrError.NONE,
                    snackbarHostState = SnackbarHostState(),
                    callBacks = noOpCallbacks,
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
