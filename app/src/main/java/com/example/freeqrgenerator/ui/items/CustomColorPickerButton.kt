package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.example.freeqrgenerator.MainState
import io.mhssn.colorpicker.ColorPicker
import io.mhssn.colorpicker.ColorPickerType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomColorPickerButton(state: MainState) {
    if (state.showColorPicker) {
        AlertDialog(
            onDismissRequest = {
                state.showColorPicker = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                Column {
                    ColorPicker(
                        type = ColorPickerType.Classic()
                    ) {
                        if (state.selectorMode == ColorSelector.FOREGROUND) {
                            state.foregroundColor = it
                        } else if (state.selectorMode == ColorSelector.BACKGROUND) {
                            state.backgroundColor = it
                        }
                    }
                }
            }
        }

    }
}

enum class ColorSelector {
    FOREGROUND, BACKGROUND, NONE
}

