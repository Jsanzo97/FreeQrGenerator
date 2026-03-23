package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freeqrgenerator.error.FreeQrError
import com.example.freeqrgenerator.presentation.ColorSelectorMode
import com.example.freeqrgenerator.presentation.MainViewModel
import com.example.freeqrgenerator.presentation.QrLayoutCallBacks
import com.example.freeqrgenerator.resources.Res
import com.example.freeqrgenerator.resources.app_name
import com.example.freeqrgenerator.resources.qr_background_color
import com.example.freeqrgenerator.resources.qr_choose_image
import com.example.freeqrgenerator.resources.qr_corners
import com.example.freeqrgenerator.resources.qr_corners_radius
import com.example.freeqrgenerator.resources.qr_customize
import com.example.freeqrgenerator.resources.qr_image_saved
import com.example.freeqrgenerator.resources.qr_main_color
import com.example.freeqrgenerator.resources.qr_save_image
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QrLayout(
    viewModel: MainViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val imageSavedMessage = stringResource(Res.string.qr_image_saved)

    LaunchedEffect(Unit) {
        viewModel.snackbarEvents.collect {
            snackbarHostState.showSnackbar(
                message = imageSavedMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }

    val callbacks = remember(viewModel) {
        object : QrLayoutCallBacks {
            override fun onUrlUpdated(url: String) {
                viewModel.updateUrl(url)
            }

            override fun onShowColorPicker(colorSelectorMode: ColorSelectorMode) {
                viewModel.showColorPicker(colorSelectorMode)
            }

            override fun onImageSelected(image: ByteArray) {
                viewModel.onImageSelected(image)
            }

            override fun onSaveImageClick() {
                viewModel.onSaveImageClick()
            }

            override fun onColorPickerDismiss() {
                viewModel.hideColorPicker()
            }

            override fun onColorSelected(color: Color) {
                viewModel.updateColorSelected(color)
            }

            override fun onShowCornersSlider() {
                viewModel.showCornersSlider()
            }

            override fun onCornersSliderDismiss() {
                viewModel.hideCornersSlider()
            }

            override fun onCornersRadiusChanged(radius: Float) {
                viewModel.updateCornersRadius(radius)
            }
        }
    }

    with(uiState) {
        QrLayoutContent(
            modifier = modifier.fillMaxWidth(),
            url = url,
            foregroundColor = foregroundColor,
            backgroundColor = backgroundColor,
            selectorMode = selectorMode,
            shouldShowColorPicker = shouldShowColorPicker,
            shouldShowCornersSlider = shouldShowCornersSlider,
            qrCornersRadius = qrCornersRadius,
            logoBytes = logoBytes,
            error = error,
            isSaving = isSaving,
            snackbarHostState = snackbarHostState,
            callBacks = callbacks,
        )
    }
}

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrLayoutContent(
    url: String,
    foregroundColor: Color,
    backgroundColor: Color,
    selectorMode: ColorSelectorMode,
    shouldShowColorPicker: Boolean,
    shouldShowCornersSlider: Boolean,
    isSaving: Boolean,
    qrCornersRadius: Float,
    logoBytes: ImmutableList<Byte>?,
    error: FreeQrError,
    snackbarHostState: SnackbarHostState,
    callBacks: QrLayoutCallBacks,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    with(callBacks) {
        Scaffold(
            modifier = modifier
                .testTag("main_screen"),
            topBar = { TopBar() },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .pointerInput(Unit) {
                            detectTapGestures {
                                focusManager.clearFocus()
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        QrViewContent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            url = url,
                            foregroundColor = foregroundColor,
                            backgroundColor = backgroundColor,
                            qrCornersRadius = qrCornersRadius,
                            logoBytes = logoBytes,
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    UrlInput(
                        modifier = Modifier.fillMaxWidth(),
                        error = error,
                        onUrlUpdated = { onUrlUpdated(it) },
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    QrCustomization(
                        foregroundColor = foregroundColor,
                        backgroundColor = backgroundColor,
                        onShowColorPicker = { onShowColorPicker(it) },
                        onShowCornersSlider = { onShowCornersSlider() },
                        onImageSelected = { onImageSelected(it) },
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                    } else {
                        CustomButton(
                            text = stringResource(Res.string.qr_save_image),
                            onClick = { onSaveImageClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(bottom = 16.dp)
                                .testTag("save_button"),
                            icon = Icons.Default.Download,
                        )
                    }
                }

                val colorPickerTitle = if (selectorMode == ColorSelectorMode.FOREGROUND) {
                    stringResource(Res.string.qr_main_color)
                } else {
                    stringResource(Res.string.qr_background_color)
                }

                val colorPickerInitialColor = if (selectorMode == ColorSelectorMode.FOREGROUND) {
                    foregroundColor
                } else {
                    backgroundColor
                }

                CustomBottomSheet(
                    visible = shouldShowColorPicker,
                    title = colorPickerTitle,
                    onDismiss = { onColorPickerDismiss() },
                    content = { modifier ->
                        ColorPickerSheetContent(
                            modifier = modifier,
                            initialColor = colorPickerInitialColor,
                            onColorSelected = { onColorSelected(it) },
                            onConfirm = { onColorPickerDismiss() },
                        )
                    },
                )

                CustomBottomSheet(
                    visible = shouldShowCornersSlider,
                    title = stringResource(Res.string.qr_corners_radius),
                    onDismiss = { onCornersSliderDismiss() },
                    content = { modifier ->
                        CustomSliderSheetContent(
                            modifier = modifier,
                            qrCornersRadius = qrCornersRadius,
                            onCornersSliderDismiss = { onCornersSliderDismiss() },
                            onCornersRadiusChanged = { onCornersRadiusChanged(it) },
                        )
                    },
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(
                stringResource(Res.string.app_name),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Composable
private fun QrCustomization(
    foregroundColor: Color,
    backgroundColor: Color,
    onShowColorPicker: (ColorSelectorMode) -> Unit,
    onShowCornersSlider: () -> Unit,
    onImageSelected: (ByteArray) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(Res.string.qr_customize),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CustomButton(
                text = stringResource(Res.string.qr_main_color),
                containerColor = foregroundColor,
                borderColor = MaterialTheme.colorScheme.outline,
                onClick = { onShowColorPicker(ColorSelectorMode.FOREGROUND) },
                isCircular = true,
            )

            CustomButton(
                text = stringResource(Res.string.qr_background_color),
                containerColor = backgroundColor,
                borderColor = MaterialTheme.colorScheme.outline,
                onClick = { onShowColorPicker(ColorSelectorMode.BACKGROUND) },
                isCircular = true,
            )

            CustomButton(
                text = stringResource(Res.string.qr_corners),
                icon = Icons.Default.RoundedCorner,
                onClick = { onShowCornersSlider() },
                isCircular = true,
            )

            CustomImagePicker(
                text = stringResource(Res.string.qr_choose_image),
                onImageSelected = onImageSelected,
            )
        }
    }
}

@Preview
@Composable
private fun QrLayoutContentPreview() {
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
            snackbarHostState = remember { SnackbarHostState() },
            callBacks = object : QrLayoutCallBacks {
                override fun onUrlUpdated(url: String) {}
                override fun onShowColorPicker(colorSelectorMode: ColorSelectorMode) {}
                override fun onImageSelected(image: ByteArray) {}
                override fun onSaveImageClick() {}
                override fun onColorPickerDismiss() {}
                override fun onColorSelected(color: Color) {}
                override fun onShowCornersSlider() {}
                override fun onCornersSliderDismiss() {}
                override fun onCornersRadiusChanged(radius: Float) {}
            },
        )
    }
}
