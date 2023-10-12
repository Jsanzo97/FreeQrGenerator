package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(modifier: Modifier, text: String, onClickListener: () -> Unit) {
    Button(
        shape = RoundedCornerShape(10),
        onClick = { onClickListener() },
        modifier = modifier
    ) {
        Text(
            text = text,
        )
    }
}