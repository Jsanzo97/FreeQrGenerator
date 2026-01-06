package com.example.freeqrgenerator.di

import com.example.freeqrgenerator.data.IosImageRepository
import com.example.freeqrgenerator.data.IosPermissionRepository
import com.example.freeqrgenerator.domain.repository.ImageRepository
import com.example.freeqrgenerator.domain.repository.PermissionRepository
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<ImageRepository> { IosImageRepository() }
    single<PermissionRepository> { IosPermissionRepository() }
}