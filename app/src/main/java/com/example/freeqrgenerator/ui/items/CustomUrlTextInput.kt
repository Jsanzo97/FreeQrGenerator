package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
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
import com.example.freeqrgenerator.R

@Composable
fun UrlInput(
    onUpdateUrl: (url: String) -> Unit,
    isErrorOnUrl: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var text: String by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onUpdateUrl(text)
            }
        ),
        value = text,
        onValueChange = {
            text = it
            onUpdateUrl(text)
        },
        label = { Text(stringResource(id = R.string.qr_introduce_url)) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red,
        ),
        isError = isErrorOnUrl,
        trailingIcon = {
            if (isErrorOnUrl) {
                Icon(
                    Icons.Filled.Info,
                    "error",
                    tint = Color.Red
                )
            }
        }
    )
}
