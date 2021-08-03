package com.sarftec.lessonsinlife.repository.impl

import com.sarftec.lessonsinlife.database.Database
import com.sarftec.lessonsinlife.database.model.Category
import com.sarftec.lessonsinlife.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
   private val database: Database
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        return database.categoryDao().categories()
    }
}