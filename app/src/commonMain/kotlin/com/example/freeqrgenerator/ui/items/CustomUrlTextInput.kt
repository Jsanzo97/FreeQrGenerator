package com.example.freeqrgenerator.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.freeqrgenerator.error.FreeQrError
import com.example.freeqrgenerator.resources.Res
import com.example.freeqrgenerator.resources.common_error
import com.example.freeqrgenerator.resources.qr_introduce_url
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun UrlInput(
    modifier: Modifier = Modifier,
    error: FreeQrError,
    onUrlUpdated: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var text: String by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .testTag("url_input"),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onUrlUpdated(text)
            },
        ),
        value = text,
        onValueChange = {
            text = it
            onUrlUpdated(text)
        },
        label = { Text(stringResource(Res.string.qr_introduce_url)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorTextColor = MaterialTheme.colorScheme.error,
            errorPlaceholderColor = MaterialTheme.colorScheme.error,
        ),
        isError = error == FreeQrError.URL_EMPTY,
        trailingIcon = {
            if (error == FreeQrError.URL_EMPTY) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(Res.string.common_error),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}

@Preview
@Composable
private fun UrlInputPreview() {
    FreeQrGeneratorTheme {
        UrlInput(
            error = FreeQrError.NONE,
            onUrlUpdated = {},
        )
    }
}

@Preview
@Composable
private fun UrlInputErrorPreview() {
    FreeQrGeneratorTheme {
        UrlInput(
            error = FreeQrError.URL_EMPTY,
            onUrlUpdated = {},
        )
    }
}
