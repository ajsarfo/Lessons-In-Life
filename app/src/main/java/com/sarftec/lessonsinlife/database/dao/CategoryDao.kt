package com.sarftec.lessonsinlife.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sarftec.lessonsinlife.database.CATEGORY_TABLE_NAME
import com.sarftec.lessonsinlife.database.model.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categories: List<Category>)

    @Query("select * from $CATEGORY_TABLE_NAME")
    suspend fun categories() : List<Category>
}