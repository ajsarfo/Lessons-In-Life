package com.sarftec.lessonsinlife.repository

import com.sarftec.lessonsinlife.database.model.Category


interface CategoryRepository {
    suspend fun getCategories() : List<Category>
}