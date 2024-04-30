package com.example.freeqrgenerator.ui.items

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freeqrgenerator.MainActivityViewModel
import com.example.freeqrgenerator.R
import com.example.freeqrgenerator.error.FreeQrError
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import com.example.freeqrgenerator.ui.utils.Constants
import com.example.freeqrgenerator.ui.utils.Constants.ColorSelectorType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRLayout(
    viewModel: MainActivityViewModel = viewModel()
) {
    val context = LocalContext.current
    val contentResolver = LocalContext.current.contentResolver

    val writePermissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val qrGenerated by viewModel.qrGenerated.collectAsStateWithLifecycle()
    val bitmapGenerated by viewModel.bitmapGenerated.collectAsStateWithLifecycle()
    val showColorPicker by viewModel.shouldShowColorPicker.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val foregroundColor by viewModel.foregroundColor.collectAsStateWithLifecycle()
    val backgroundColor by viewModel.backgroundColor.collectAsStateWithLifecycle()

    Content(
        qrGenerated = qrGenerated,
        bitmapGenerated = bitmapGenerated,
        isErrorOnUrl = error == FreeQrError.URL_EMPTY,
        showColorPicker = showColorPicker,
        foregroundColor = foregroundColor,
        backgroundColor = backgroundColor,
        onUpdateUrl = { viewModel.updateUrl(it) },
        onImageSelected = { viewModel.generateBitmapFromUri(it, contentResolver) },
        showColorSelector = { viewModel.showColorPicker(it) },
        onColorSelected = { viewModel.updateColorSelected(it) },
        onBoundsCalculated = { viewModel.setQrViewAndWindow(it, (context as Activity).window) },
        onSaveQr = {
            if (writePermissionState.status.isGranted || Build.VERSION.SDK_INT >= 33) {
                viewModel.saveImage(
                    context = context,
                    folderName = "FreeQr",
                    callback = {
                        Toast.makeText(
                            context,
                            "Image saved in $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            } else {
                writePermissionState.launchPermissionRequest()
            }
        },
        onDismissAction = { viewModel.hideColorPicker() },
    )
}

@Composable
fun Content(
    qrGenerated: Drawable?,
    bitmapGenerated: Bitmap?,
    isErrorOnUrl: Boolean,
    showColorPicker: Boolean,
    foregroundColor: Color,
    backgroundColor: Color,
    onUpdateUrl: (url: String) -> Unit,
    onImageSelected: (uri: Uri?) -> Unit,
    showColorSelector: (type: ColorSelectorType) -> Unit,
    onColorSelected: (color: Color) -> Unit,
    onBoundsCalculated: (bounds: Rect?) -> Unit,
    onSaveQr: () -> Unit,
    onDismissAction: () -> Unit
) {

    val localFocusManager = LocalFocusManager.current

    var uri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) {
        uri = it
        onImageSelected(uri)
    }

    if (showColorPicker) {
        QRColorPickerButton(
            onColorSelected = { onColorSelected(it) },
            onDismissAction = { onDismissAction() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = FreeQrGeneratorTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QRView(
            modifier = Modifier
                .padding(top = 32.dp),
            qrGenerated = qrGenerated,
            bitmapGenerated = bitmapGenerated,
            onBoundsCalculated = { onBoundsCalculated(it) }
        )
        QRUrlInput(
            modifier = Modifier
                .padding(
                    vertical = 32.dp,
                    horizontal = 16.dp
                ),
            onUpdateUrl = { onUpdateUrl(it) },
            isErrorOnUrl = isErrorOnUrl
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            QRCircularButton(
                text = stringResource(id = R.string.qr_main_color),
                backgroundColor = foregroundColor,
                borderColor = FreeQrGeneratorTheme.colors.opposite,
                onClickListener = { showColorSelector(ColorSelectorType.FOREGROUND) }
            )

            QRCircularButton(
                text = stringResource(id = R.string.qr_background_color),
                backgroundColor = backgroundColor,
                borderColor = FreeQrGeneratorTheme.colors.opposite,
                onClickListener = { showColorSelector(ColorSelectorType.BACKGROUND) }
            )

            QRCircularButton(
                text = stringResource(id = R.string.qr_choose_image),
                icon = Icons.Default.CameraAlt,
                onClickListener = {
                    launcher.launch(Constants.IMAGE_LAUNCHER)
                }
            )

            QRCircularButton(
                text = stringResource(id = R.string.qr_save_image),
                icon = Icons.Default.SaveAs,
                onClickListener = {
                    localFocusManager.clearFocus(force = true)
                    onSaveQr()
                }
            )
        }
    }
}
@Preview(showBackground = true, widthDp = 420)
@Composable
fun QrLayoutPreview() {
    Content(
        qrGenerated = null,
        bitmapGenerated = null,
        isErrorOnUrl = false,
        showColorPicker = false,
        foregroundColor = Color.White,
        backgroundColor = Color.Black,
        onUpdateUrl = {},
        onImageSelected = {},
        showColorSelector = {},
        onColorSelected = {},
        onBoundsCalculated = {},
        onSaveQr = { },
        onDismissAction = {}
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun QrLayoutUrlErrorPreview() {
    Content(
        qrGenerated = null,
        bitmapGenerated = null,
        isErrorOnUrl = true,
        showColorPicker = false,
        foregroundColor = Color.White,
        backgroundColor = Color.Black,
        onUpdateUrl = {},
        onImageSelected = {},
        showColorSelector = {},
        onColorSelected = {},
        onBoundsCalculated = {},
        onSaveQr = { },
        onDismissAction = {}
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun QrLayoutColorPickerPreview() {
    Content(
        qrGenerated = null,
        bitmapGenerated = null,
        isErrorOnUrl = false,
        showColorPicker = true,
        foregroundColor = Color.White,
        backgroundColor = Color.Black,
        onUpdateUrl = {},
        onImageSelected = {},
        showColorSelector = {},
        onColorSelected = {},
        onBoundsCalculated = {},
        onSaveQr = { },
        onDismissAction = {}
    )
}