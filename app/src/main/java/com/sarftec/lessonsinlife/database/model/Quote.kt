package com.sarftec.lessonsinlife.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sarftec.lessonsinlife.database.QUOTE_TABLE_NAME

@Entity(
    tableName = QUOTE_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )
    ]
)
class Quote(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(index = true) val categoryId: Int,
    val message: String,
    var isFavorite: Boolean = false
)