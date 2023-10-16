package com.example.freeqrgenerator

import android.app.Activity
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.freeqrgenerator.ui.items.ColorSelector
import com.example.freeqrgenerator.ui.items.QrGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainActivityViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private fun generateQr() {
        _uiState.update {
            with(uiState.value) {
                it.copy(qrGenerated =
                    QrGenerator().processQr(
                        foregroundColor,
                        backgroundColor,
                        url
                    )
                )
            }
        }
    }

    fun updateColorSelected(color: Color) {
        with(uiState.value) {
            if (uiState.value.selectorMode == ColorSelector.FOREGROUND) {
                _uiState.update {
                    it.copy(foregroundColor = color)
                }
            } else if (uiState.value.selectorMode == ColorSelector.BACKGROUND) {
                _uiState.update {
                    it.copy(backgroundColor = color)
                }
            }
            if (url.isNotEmpty()) {
                _uiState.update {
                    it.copy(error = MainError.NONE)
                }
                generateQr()
            }
        }
    }

    fun showColorPicker(selectorMode: ColorSelector) {
        _uiState.update {
            it.copy(
                shouldShowColorPicker = true,
                selectorMode = selectorMode
            )
        }
    }

    fun hideColorPicker() {
        _uiState.update {
            it.copy(
                shouldShowColorPicker = false
            )
        }
    }

    fun generateBitmapFromUri(uri: Uri?, contentResolver: ContentResolver) {
        _uiState.update {
            it.copy(
                bitmap = uri?.let { uri ->
                    if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    } else {
                        val source = ImageDecoder.createSource(contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    }
                }
            )
        }
    }

    fun updateUrl(url: String) {
        if (url.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    url = url,
                    error = MainError.NONE
                )
            }
            generateQr()
        } else {
            _uiState.update {
                it.copy(
                    error = MainError.URL_EMPTY,
                    qrGenerated = null
                )
            }
        }
    }

    fun handleEmptyUrlError() {
        _uiState.update {
            it.copy(
                error = MainError.URL_EMPTY
            )
        }
    }

    fun setQrViewAndWindow(bounds: Rect?, window: Window) {
        _uiState.update {
            it.copy(
                qrBounds = bounds,
                qrWindow = window
            )
        }
    }

    fun getBitmapFromView(callback: (Bitmap) -> Unit) {
        with(uiState.value) {
            if (qrBounds != null && qrWindow != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    val bitmap = Bitmap.createBitmap(
                        qrBounds.width.toInt(),
                        qrBounds.height.toInt(),
                        Bitmap.Config.ARGB_8888
                    )

                    callback(bitmap)
                } else {
                    PixelCopy.request(
                        qrWindow,
                        bounds.toAndroidRect(),
                        bitmap
                    )
                }
            }
        }
    }
}