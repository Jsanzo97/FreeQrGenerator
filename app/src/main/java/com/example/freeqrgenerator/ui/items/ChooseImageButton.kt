package com.example.freeqrgenerator.ui.items

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freeqrgenerator.MainActivityViewModel

@Composable
fun ChooseImageButton(viewModel: MainActivityViewModel = viewModel(), modifier: Modifier, text: String) {
    val contentResolver = LocalContext.current.contentResolver

    var uri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) {
        uri = it
    }

    CustomButton(
        modifier = modifier,
        text = text
    ) {
        launcher.launch("image/*")
    }

    viewModel.generateBitmapFromUri(uri, contentResolver)
}