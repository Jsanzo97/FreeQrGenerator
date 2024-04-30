package com.example.freeqrgenerator

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.freeqrgenerator.error.FreeQrError
import com.example.freeqrgenerator.generator.QRGenerator
import com.example.freeqrgenerator.ui.utils.Constants.ColorSelectorType
import com.example.freeqrgenerator.ui.utils.Constants.Companion.STRING_EMPTY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt
import androidx.compose.ui.geometry.Rect as ComposeRect


class MainActivityViewModel: ViewModel() {
    private val _url = MutableStateFlow(STRING_EMPTY)
    val url = _url.asStateFlow()

    private val _qrGenerated: MutableStateFlow<Drawable?> = MutableStateFlow(null)
    val qrGenerated = _qrGenerated.asStateFlow()

    private val _bitmapGenerated: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val bitmapGenerated = _bitmapGenerated.asStateFlow()

    private val _shouldShowColorPicker = MutableStateFlow(false)
    val shouldShowColorPicker = _shouldShowColorPicker.asStateFlow()

    private val _foregroundColor = MutableStateFlow(Color.Black)
    val foregroundColor = _foregroundColor.asStateFlow()

    private val _backgroundColor = MutableStateFlow(Color.White)
    val backgroundColor = _backgroundColor.asStateFlow()

    private val _selectorMode = MutableStateFlow(ColorSelectorType.NONE)
    val selectorMode = _selectorMode.asStateFlow()

    private val _error = MutableStateFlow(FreeQrError.NONE)
    val error = _error.asStateFlow()

    private val _qrBounds: MutableStateFlow<ComposeRect?> = MutableStateFlow(null)
    val qrBounds = _qrBounds.asStateFlow()

    private val _qrWindow: MutableStateFlow<Window?> = MutableStateFlow(null)
    val qrWindow = _qrWindow.asStateFlow()

    private fun generateQr() {
        _qrGenerated.value = QRGenerator()
            .processQr(
                _foregroundColor.value,
                _backgroundColor.value,
                _url.value
            )
    }

    fun updateColorSelected(color: Color) {
            if (_selectorMode.value == ColorSelectorType.FOREGROUND) {
                _foregroundColor.value = color
            } else if (_selectorMode.value == ColorSelectorType.BACKGROUND) {
                _backgroundColor.value = color
            }
            if (_url.value.isNotEmpty()) {
                _error.value = FreeQrError.NONE
                generateQr()
            }
    }

    fun showColorPicker(selectorMode: ColorSelectorType) {
        _shouldShowColorPicker.value = true
        _selectorMode.value = selectorMode
    }

    fun hideColorPicker() {
        _shouldShowColorPicker.value = false
    }

    fun generateBitmapFromUri(uri: Uri?, contentResolver: ContentResolver) {
        _bitmapGenerated.value = uri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    fun updateUrl(url: String) {
        if (url.isNotEmpty()) {
            _url.value = url
            _error.value = FreeQrError.NONE
            generateQr()
        } else {
            handleEmptyUrlError()
        }
    }

    private fun handleEmptyUrlError() {
        _error.value = FreeQrError.URL_EMPTY
        _qrGenerated.value = null
    }

    fun setQrViewAndWindow(bounds: ComposeRect?, window: Window) {
        _qrBounds.value = bounds
        _qrWindow.value = window
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getBitmapFromView(callback: (Bitmap) -> Unit) {
        if (_qrBounds.value != null && _qrWindow.value != null) {

            val bitmap = Bitmap.createBitmap(
                _qrBounds.value!!.width.roundToInt(),
                _qrBounds.value!!.height.roundToInt(),
                Bitmap.Config.ARGB_8888
            )

            val rect = Rect(
                _qrBounds.value!!.left.roundToInt(),
                _qrBounds.value!!.top.roundToInt(),
                _qrBounds.value!!.right.roundToInt(),
                _qrBounds.value!!.bottom.roundToInt()
            )
            PixelCopy.request(
                _qrWindow.value!!,
                rect,
                bitmap,
                {
                    callback(bitmap)
                },
                Handler(Looper.getMainLooper())
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveImage(context: Context, folderName: String, callback: (String) -> Unit) {
        if (_url.value.isEmpty()) {
            handleEmptyUrlError()
        } else {
            getBitmapFromView {
                if (Build.VERSION.SDK_INT >= 29) {
                    val path = "Pictures/$folderName"
                    val values = contentValues()
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
                    values.put(MediaStore.Images.Media.IS_PENDING, true)

                    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    if (uri != null) {
                        saveImageToStream(it, context.contentResolver.openOutputStream(uri))
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
                    saveImageToStream(it, FileOutputStream(file))
                    val values = contentValues()
                    values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    callback(directory.path)
                }
            }
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