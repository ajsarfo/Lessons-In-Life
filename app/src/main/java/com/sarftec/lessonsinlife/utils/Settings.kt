package com.sarftec.lessonsinlife.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val IS_DARK_MODE = booleanPreferencesKey("is_app_in_dark_mode")

val Context.dataStore by preferencesDataStore(name = "settings_store")

fun <T> Context.readSettings(key: Preferences.Key<T>, default: T): Flow<T> {
    return dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw Exception("Setting data store exception occurred")
        }
        .map { preferences ->
            preferences[key] ?: default
        }

}

suspend fun <T> Context.editSettings(key: Preferences.Key<T>, value: T) {
    dataStore.edit { preferences ->
        preferences[key] = value
    }
}