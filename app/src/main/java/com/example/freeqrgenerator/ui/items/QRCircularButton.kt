package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import com.example.freeqrgenerator.ui.utils.Constants.Companion.STRING_EMPTY

@Composable
fun QRCircularButton(
    modifier: Modifier = Modifier,
    text: String = STRING_EMPTY,
    icon: ImageVector? = null,
    backgroundColor: Color = FreeQrGeneratorTheme.colors.primary,
    borderColor: Color? = null,
    onClickListener: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = modifier
                .size(48.dp),
            border = if (borderColor != null) {
                BorderStroke(
                    width = 1.dp,
                    color = borderColor
                )
            } else {
                null
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = Color.White
            ),
            onClick = { onClickListener() },
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = "button_icon"
                )
            }
        }

        Text(
            text = text,
            color = FreeQrGeneratorTheme.colors.opposite
        )
    }

}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun QRCircularButtonPreview() {
    QRCircularButton(
        onClickListener = {}
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun QRCircularButtonBorderPreview() {
    QRCircularButton(
        borderColor = Color.Black,
        backgroundColor = Color.White,
        onClickListener = {}
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun QRCircularButtonTextPreview() {
    QRCircularButton(
        text = "Main Color",
        onClickListener = {}
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun QRCircularButtonIconPreview() {
    QRCircularButton(
        icon = Icons.Default.Close,
        onClickListener = {}
    )
}