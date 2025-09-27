package com.skrainyukov.settings.di

import com.skrainyukov.settings.dataStore.DataStoreServiceImpl
import com.skrainyukov.settings.ui.SettingsViewModel
import com.skrainyukov.settings.ui.contract.DataStoreService
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<DataStoreService> { DataStoreServiceImpl(androidApplication()) }
    viewModel { SettingsViewModel(get()) }
}