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
import androidx.compose.material.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.freeqrgenerator.R
import io.mhssn.colorpicker.ColorPicker
import io.mhssn.colorpicker.ColorPickerType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QRColorPickerButton(
    onColorSelected: (color: Color) -> Unit,
    onDismissAction: () -> Unit
) {

    var color by remember { mutableStateOf(Color.White) }

    Dialog(
        onDismissRequest = { onDismissAction() }
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp)),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ColorPicker(
                    type = ColorPickerType.Circle(),
                    onPickedColor = {
                        color = it
                        onColorSelected(it)
                    }
                )
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
                    QRButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.qr_save_color),
                        onClickListener = { onDismissAction() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun QRColorPickerButtonPreview() {
    QRColorPickerButton(
        onColorSelected = {},
        onDismissAction = {}
    )
}



