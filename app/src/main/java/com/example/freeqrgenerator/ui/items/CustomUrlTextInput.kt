package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freeqrgenerator.MainActivityViewModel
import com.example.freeqrgenerator.MainError
import com.example.freeqrgenerator.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UrlInput(viewModel: MainActivityViewModel = viewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val uiState by viewModel.uiState.collectAsState()

    var text: String by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
                viewModel.updateUrl(text)
            }
        ),
        value = text,
        onValueChange = {
            text = it
            viewModel.updateUrl(text)
        },
        label = { Text(stringResource(id = R.string.qr_introduce_url)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            errorBorderColor = Color.Red,
            errorTextColor = Color.Red,
            errorPlaceholderColor = Color.Red,
        ),
        label = { Text("Introduce your url") },
        isError = uiState.error == MainError.URL_EMPTY,
        trailingIcon = {
            if (uiState.error == MainError.URL_EMPTY) {
                Icon(
                    Icons.Filled.Info,
                    "error",
                    tint = Color.Red
                )
            }
        }
    )
}
