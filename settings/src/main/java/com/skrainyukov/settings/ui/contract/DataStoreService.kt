package com.skrainyukov.settings.ui.contract

import com.skrainyukov.settings.dataStore.SettingContainer
import kotlinx.coroutines.flow.StateFlow

interface DataStoreService {
    val settingData: StateFlow<SettingContainer>
    suspend fun saveSetting(periodic: Long, delayed: Long)
    suspend fun readSetting()
}