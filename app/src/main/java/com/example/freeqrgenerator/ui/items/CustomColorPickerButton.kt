package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freeqrgenerator.MainActivityViewModel
import io.mhssn.colorpicker.ColorPicker
import io.mhssn.colorpicker.ColorPickerType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomColorPickerButton(viewModel: MainActivityViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var color by remember { mutableStateOf(Color.White) }

    if (uiState.shouldShowColorPicker) {
        AlertDialog(
            onDismissRequest = {
                viewModel.hideColorPicker()
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
                        type = ColorPickerType.Circle()
                    ) {
                        color = it
                        viewModel.updateColorSelected(it)
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
                                .fillMaxWidth(),
                            text = "Close"
                        ) {
                            viewModel.hideColorPicker()
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

