package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CustomButton(modifier: Modifier = Modifier, text: String, onClickListener: () -> Unit) {
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