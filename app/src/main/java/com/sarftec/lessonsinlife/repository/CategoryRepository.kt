package com.sarftec.lessonsinlife.repository

import androidx.paging.PagingData
import com.sarftec.lessonsinlife.database.model.Category
import kotlinx.coroutines.flow.Flow


interface CategoryRepository {
    suspend fun getCategories() : List<Category>
    fun getCategoryFlow() : Flow<PagingData<Category>>
}