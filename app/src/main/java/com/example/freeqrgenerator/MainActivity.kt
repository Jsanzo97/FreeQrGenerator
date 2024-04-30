package com.example.freeqrgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.freeqrgenerator.navigation.SetupNavGraph
import com.example.freeqrgenerator.ui.items.QRLayout
import com.example.freeqrgenerator.ui.theme.FreeQrGeneratorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeQrGeneratorTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}

