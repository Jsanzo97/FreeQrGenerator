package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.resources.Res
import com.example.freeqrgenerator.resources.common_confirm
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomSliderSheetContent(
    qrCornersRadius: Float,
    onCornersSliderDismiss: () -> Unit,
    onCornersRadiusChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Slider(
            value = qrCornersRadius,
            onValueChange = onCornersRadiusChanged,
            valueRange = 0f..0.5f
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            text = stringResource(Res.string.common_confirm),
            onClick = onCornersSliderDismiss,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun CustomSliderSheetContentPreview() {
    FreeQrGeneratorTheme {
        CustomSliderSheetContent(
            qrCornersRadius = 0.5f,
            onCornersSliderDismiss = {},
            onCornersRadiusChanged = {}
        )
    }
}