package com.sarftec.lessonsinlife.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sarftec.lessonsinlife.database.dao.CategoryDao
import com.sarftec.lessonsinlife.database.dao.QuoteDao
import com.sarftec.lessonsinlife.database.model.Category
import com.sarftec.lessonsinlife.database.model.Quote

@Database(entities = [Quote::class, Category::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
    abstract fun categoryDao(): CategoryDao
   }