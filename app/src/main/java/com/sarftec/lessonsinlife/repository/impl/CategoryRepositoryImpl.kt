package com.sarftec.lessonsinlife.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sarftec.lessonsinlife.database.Database
import com.sarftec.lessonsinlife.database.model.Category
import com.sarftec.lessonsinlife.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
   private val database: Database
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        return database.categoryDao().categories()
    }

    override fun getCategoryFlow(): Flow<PagingData<Category>> {
        return Pager(PagingConfig(20, enablePlaceholders = true)) {
            database.categoryDao().getPagingSource()
        }.flow
    }
}