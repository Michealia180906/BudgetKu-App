package com.michealia0091.budgetku.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(
    name = "settings"
)

class SettingsDataStore(
    private val context: Context
) {
    private val layoutKey = booleanPreferencesKey("show_list")

    val layoutFlow = context.dataStore.data.map { preferences ->
        preferences[layoutKey] ?: true
    }

    suspend fun saveLayout(showList: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[layoutKey] = showList
        }
    }
}