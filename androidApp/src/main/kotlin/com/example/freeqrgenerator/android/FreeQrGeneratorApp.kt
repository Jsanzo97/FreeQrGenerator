package com.example.freeqrgenerator.android

import android.app.Application
import com.example.freeqrgenerator.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FreeQrGeneratorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@FreeQrGeneratorApp)
            modules(initKoin())
        }
    }
}
