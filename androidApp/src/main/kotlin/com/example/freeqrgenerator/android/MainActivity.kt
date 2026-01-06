package com.example.freeqrgenerator.android

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.freeqrgenerator.domain.repository.PermissionRepository
import com.example.freeqrgenerator.navigation.SetupNavGraph
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val permissionRepository: PermissionRepository by inject()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionRepository.permissionRequests
            .onEach { 
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            .launchIn(lifecycleScope)

        setContent {
            FreeQrGeneratorTheme {
                SetupNavGraph()
            }
        }
    }
}
