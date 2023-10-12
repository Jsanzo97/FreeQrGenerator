package com.example.freeqrgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.freeqrgenerator.ui.items.QrLayout
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme


class MainActivity : ComponentActivity() {

    private var state = MainState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeQrGeneratorTheme {
                QrLayout(state)
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1280, widthDp = 720)
@Composable
fun Preview() {
    FreeQrGeneratorTheme {
        QrLayout(MainState())
    }
}