package com.example.freeqrgenerator.di

import com.example.freeqrgenerator.data.QrRepositoryImpl
import com.example.freeqrgenerator.domain.repository.QrRepository
import com.example.freeqrgenerator.domain.usecase.CheckWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.CheckWritePermissionsUseCaseImpl
import com.example.freeqrgenerator.domain.usecase.GenerateQrUseCase
import com.example.freeqrgenerator.domain.usecase.GenerateQrUseCaseImpl
import com.example.freeqrgenerator.domain.usecase.RequestWritePermissionsUseCase
import com.example.freeqrgenerator.domain.usecase.RequestWritePermissionsUseCaseImpl
import com.example.freeqrgenerator.domain.usecase.SaveImageUseCase
import com.example.freeqrgenerator.domain.usecase.SaveImageUseCaseImpl
import com.example.freeqrgenerator.presentation.MainViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    single<QrRepository> { QrRepositoryImpl() }
    single<CheckWritePermissionsUseCase> { CheckWritePermissionsUseCaseImpl(get()) }
    single<RequestWritePermissionsUseCase> { RequestWritePermissionsUseCaseImpl(get()) }
    single<SaveImageUseCase> { SaveImageUseCaseImpl(get()) }
    single<GenerateQrUseCase> { GenerateQrUseCaseImpl(get()) }
    viewModelOf(::MainViewModel)
}

expect val platformModule: Module

fun initKoin() = listOf(commonModule, platformModule)
