package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.MainState
import io.mhssn.colorpicker.ColorPicker
import io.mhssn.colorpicker.ColorPickerType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomColorPickerButton(state: MainState) {
    var color by remember { mutableStateOf(Color.White) }

    if (state.showColorPicker) {
        AlertDialog(
            onDismissRequest = {
                state.showColorPicker = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp)),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(24.dp)
                ) {
                    ColorPicker(
                        type = ColorPickerType.Circle(
                            showAlphaBar = false,
                        ),
                    ) {
                        color = it
                    }
                    Box(
                        modifier = Modifier
                            .height(22.dp)
                            .width(200.dp)
                            .clip(RoundedCornerShape(50))
                            .border(1.dp, Color.Black, RoundedCornerShape(50))
                            .background(color)
                    )
                    Row(
                        modifier = Modifier
                            .width(200.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        CustomButton(
                            modifier = Modifier
                                .weight(5f),
                            text = "Close"
                        ) {
                            state.showColorPicker = false
                        }
                        CustomButton(
                            modifier = Modifier
                                .weight(5f),
                            text = "Select"
                        ) {
                            if (state.selectorMode == ColorSelector.FOREGROUND) {
                                state.foregroundColor = color
                            } else if (state.selectorMode == ColorSelector.BACKGROUND) {
                                state.backgroundColor = color
                            }
                            state.showColorPicker = false
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

