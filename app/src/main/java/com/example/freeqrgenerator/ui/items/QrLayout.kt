package com.example.freeqrgenerator.ui.items

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freeqrgenerator.MainActivityViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QrLayout(viewModel: MainActivityViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val writePermissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    CustomColorPickerButton(
        viewModel
    )
    Column {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
        ) {
            QrView(viewModel)
        }
        Row(
            modifier = Modifier
                .weight(3f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Bottom),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UrlInput(viewModel)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Main color")
                        {
                            viewModel.showColorPicker(ColorSelector.FOREGROUND)
                        }
                        ChooseImageButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Choose Image",
                            viewModel = viewModel
                        )
                    }
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Background color")
                        {
                            viewModel.showColorPicker(ColorSelector.BACKGROUND)
                        }
                        CustomButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
                            text = "Save Qr"
                        ) {
                            if (uiState.url.isEmpty()) {
                                viewModel.handleEmptyUrlError()
                            } else {
                                if (writePermissionState.status.isGranted) {
                                    viewModel.saveImage(context, "FreeQr") {
                                        Toast.makeText(context, "Image saved in " + it, Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    writePermissionState.launchPermissionRequest()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}