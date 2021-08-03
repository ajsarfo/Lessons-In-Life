package com.sarftec.lessonsinlife.database

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.sarftec.lessonsinlife.database.json.JsonCategory
import com.sarftec.lessonsinlife.database.json.JsonQuote
import com.sarftec.lessonsinlife.database.model.Category
import com.sarftec.lessonsinlife.utils.editSettings
import com.sarftec.lessonsinlife.utils.readSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DatabaseSetup @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: Database
) {

    suspend fun prepareDatabase() {
        context.assets.open("data/categories.json").bufferedReader().use { reader ->
            val categories: List<JsonCategory> = Json.decodeFromString(reader.readText())
            database.categoryDao().insert(
                categories.map {
                    it.toCategory()
                }
            )
        }
        context.assets.open("data/quotes.json").bufferedReader().use { reader ->
            val quotes: List<JsonQuote> = Json.decodeFromString(reader.readText())
            database.quoteDao().insert(
                quotes.map {
                    it.toQuote()
                }
            )
        }
        context.editSettings(isAppCreated, true)
    }

    suspend fun isPrepared() : Boolean {
        return context.readSettings(isAppCreated, false).first()
    }

    companion object {
        private val isAppCreated = booleanPreferencesKey("is_app_created")
    }
}