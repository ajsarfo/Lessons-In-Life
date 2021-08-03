package com.sarftec.lessonsinlife.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sarftec.lessonsinlife.database.CATEGORY_TABLE_NAME

@Entity(tableName = CATEGORY_TABLE_NAME)
class Category(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String
)