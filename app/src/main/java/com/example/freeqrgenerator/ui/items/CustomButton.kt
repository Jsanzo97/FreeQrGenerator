package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClickListener: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(10),
        onClick = { onClickListener() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
    ) {
        Text(
            text = text,
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun CustomButtonPreview() {
    CustomButton(
        text = "Test",
        onClickListener = {}
    )
}