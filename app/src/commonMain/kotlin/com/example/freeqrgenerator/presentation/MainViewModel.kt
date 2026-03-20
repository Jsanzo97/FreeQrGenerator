package com.example.freeqrgenerator.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freeqrgenerator.domain.usecase.CheckWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.GenerateQrUseCase
import com.example.freeqrgenerator.domain.usecase.RequestWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.SaveImageUseCase
import com.example.freeqrgenerator.error.FreeQrError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface QrLayoutCallBacks {
    fun onUrlUpdated(url: String)
    fun onShowColorPicker(colorSelectorMode: ColorSelectorMode)
    fun onImageSelected(image: ByteArray)
    fun onSaveImageClick()
    fun onColorPickerDismiss()
    fun onColorSelected(color: Color)
    fun onShowCornersSlider()
    fun onCornersSliderDismiss()
    fun onCornersRadiusChanged(radius: Float)
}

class MainViewModel(
    private val checkWritePermissionsUseCase: CheckWritePermissionsUseCase,
    private val requestWritePermissionsUseCase: RequestWritePermissionsUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val generateQrUseCase: GenerateQrUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private val _snackbarEvents = Channel<Unit>()
    val snackbarEvents = _snackbarEvents.receiveAsFlow()

    val permissionRequests: Flow<Unit> = requestWritePermissionsUseCase.requests

    fun updateColorSelected(color: Color) {
        _uiState.update { state ->
            when (state.selectorMode) {
                ColorSelectorMode.FOREGROUND -> state.copy(
                    foregroundColor = color,
                    error = FreeQrError.NONE
                )
                ColorSelectorMode.BACKGROUND -> state.copy(
                    backgroundColor = color,
                    error = FreeQrError.NONE
                )
                else -> state
            }
        }
    }

    fun showColorPicker(mode: ColorSelectorMode) {
        _uiState.update {
            it.copy(
                shouldShowColorPicker = true,
                selectorMode = mode,
                shouldShowCornersSlider = false
            )
        }
    }

    fun hideColorPicker() {
        _uiState.update {
            it.copy(shouldShowColorPicker = false)
        }
    }

    fun showCornersSlider() {
        _uiState.update {
            it.copy(
                shouldShowCornersSlider = true,
                shouldShowColorPicker = false,
                selectorMode = ColorSelectorMode.NONE
            )
        }
    }

    fun hideCornersSlider() {
        _uiState.update {
            it.copy(shouldShowCornersSlider = false)
        }
    }

    fun updateCornersRadius(radius: Float) {
        _uiState.update {
            it.copy(qrCornersRadius = radius)
        }
    }

    fun updateUrl(url: String) {
        _uiState.update {
            it.copy(
                url = url,
                error = if (url.isEmpty()) FreeQrError.URL_EMPTY else FreeQrError.NONE
            )
        }
    }

    fun onImageSelected(image: ByteArray) {
        _uiState.update {
            it.copy(logoBytes = image.toList())
        }
    }

    fun onSaveImageClick() {
        with(uiState.value) {
            if (url.isEmpty()) {
                _uiState.update { it.copy(error = FreeQrError.URL_EMPTY) }
                return
            }

            viewModelScope.launch(Dispatchers.Default) {
                _uiState.update { it.copy(isSaving = true) }

                try {
                    if (!checkWritePermissionsUseCase.invoke()) {
                        requestWritePermissionsUseCase.invoke()
                        _uiState.update { it.copy(isSaving = false) }
                        return@launch
                    }

                    generateQrUseCase.invoke(
                        url = url,
                        foregroundColor = foregroundColor,
                        backgroundColor = backgroundColor,
                        cornersRadius = qrCornersRadius,
                        logoBytes = logoBytes
                    ).onSuccess { bytes ->
                        saveImageUseCase.invoke(bytes)
                            .onSuccess {
                                _uiState.update { it.copy(isSaving = false) }
                                _snackbarEvents.send(Unit)
                            }
                            .onFailure {
                                _uiState.update { it.copy(isSaving = false) }
                            }
                    }.onFailure {
                        _uiState.update { it.copy(isSaving = false) }
                    }
                } catch (_: Exception) {
                    _uiState.update { it.copy(isSaving = false) }
                }
            }
        }
    }
}