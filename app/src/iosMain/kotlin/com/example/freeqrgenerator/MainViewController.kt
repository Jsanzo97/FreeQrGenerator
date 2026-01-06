package com.example.freeqrgenerator

import androidx.compose.ui.window.ComposeUIViewController
import com.example.freeqrgenerator.di.initKoin
import com.example.freeqrgenerator.navigation.SetupNavGraph
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(initKoin())
        }
    }
) {
    SetupNavGraph()
}