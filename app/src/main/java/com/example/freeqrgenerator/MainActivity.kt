package com.example.freeqrgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.freeqrgenerator.ui.items.QrLayout
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme


class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeQrGeneratorTheme {
                QrLayout(viewModel)
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1280, widthDp = 720)
@Composable
fun Preview() {
    FreeQrGeneratorTheme {
        QrLayout(MainActivityViewModel())
    }
}