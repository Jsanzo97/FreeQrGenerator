package com.example.freeqrgenerator

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import com.example.freeqrgenerator.ui.items.ColorSelector
import com.example.freeqrgenerator.ui.items.QrGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt
import androidx.compose.ui.geometry.Rect as ComposeRect


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

    fun setViewToScreenshot(view: View) {
        _uiState.update {
            it.copy(viewToScreenshot = view)
        }
    }

    fun updateBitmap(bitmap: Bitmap, resources: Resources) {
        _uiState.update {
            it.copy(qrGenerated = bitmap.toDrawable(resources))
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

    fun setQrViewAndWindow(bounds: ComposeRect?, window: Window) {
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

                val bitmap = Bitmap.createBitmap(
                    qrBounds.width.roundToInt(),
                    qrBounds.height.roundToInt(),
                    Bitmap.Config.ARGB_8888
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val rect = Rect(
                        qrBounds.left.roundToInt(),
                        qrBounds.top.roundToInt(),
                        qrBounds.right.roundToInt(),
                        qrBounds.bottom.roundToInt()
                    )
                    PixelCopy.request(
                        qrWindow,
                        rect,
                        bitmap,
                        {
                            callback(bitmap)
                        },
                        Handler(Looper.getMainLooper())
                    )
                } else {
                    bitmap.applyCanvas {
                        translate(-qrBounds.left, -qrBounds.top)
                        viewToScreenshot?.draw(this)
                    }
                    callback(bitmap)
                }
            }
        }
    }

    fun saveImage(bitmap: Bitmap, context: Context, folderName: String, callback: (String) -> Unit) {
        if (Build.VERSION.SDK_INT >= 29) {
            val path = "Pictures/" + folderName
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
            values.put(MediaStore.Images.Media.IS_PENDING, true)

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
            callback(path)
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            callback(directory.path)
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}