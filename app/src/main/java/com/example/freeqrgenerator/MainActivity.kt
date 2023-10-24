package com.example.freeqrgenerator

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.freeqrgenerator.ui.items.QrLayout
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeQrGeneratorTheme {
                QrLayout(viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 480)
@Composable
fun Preview() {
    FreeQrGeneratorTheme {
        QrLayout(MainActivityViewModel())
    }
}