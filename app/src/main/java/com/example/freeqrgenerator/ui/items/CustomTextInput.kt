package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.freeqrgenerator.MainState

@Composable
fun UrlInput(state: MainState) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.url,
        onValueChange = { state.url = it},
        label = { Text("Introduce your url") }
    )
}