package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.MainState

@Composable
fun QrLayout(state: MainState) {
    CustomColorPickerButton(
        state = state
    )
    Column {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
        ) {
            QrPreview(state)
        }

        Row(
            modifier = Modifier
                .weight(3f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Bottom),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UrlInput(state)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        ChooseImageButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Choose Image",
                            state = state
                        )
                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Main color")
                        {
                            state.showColorPicker = true
                            state.selectorMode = ColorSelector.FOREGROUND
                        }
                    }
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Generate Qr"
                        ) {
                            if (state.url.isNotEmpty()) {
                                state.qrGenerated = QrGenerator().processQr(state)
                            }
                        }
                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Background color")
                        {
                            state.showColorPicker = true
                            state.selectorMode = ColorSelector.BACKGROUND
                        }
                    }
                }
            }
        }
    }
}