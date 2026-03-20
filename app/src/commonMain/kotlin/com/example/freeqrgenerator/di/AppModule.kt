package com.example.freeqrgenerator.di

import com.example.freeqrgenerator.presentation.MainViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    viewModelOf(::MainViewModel)
}

expect val platformModule: Module

fun initKoin() = listOf(commonModule, platformModule)
