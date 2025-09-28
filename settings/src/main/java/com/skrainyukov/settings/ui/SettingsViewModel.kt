package com.skrainyukov.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skrainyukov.settings.dataStore.SettingContainer
import com.skrainyukov.settings.ui.contract.DataStoreService
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStoreService: DataStoreService
) : ViewModel() {

    fun saveSetting(periodic: Long, delayed: Long) {
        viewModelScope.launch {
            dataStoreService.saveSetting(periodic = periodic, delayed = delayed)
        }
    }

    fun getCurrentSetting(): SettingContainer {
        return dataStoreService.settingData.value
    }
}