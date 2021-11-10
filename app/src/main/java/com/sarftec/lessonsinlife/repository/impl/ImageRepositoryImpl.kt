package com.sarftec.lessonsinlife.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sarftec.lessonsinlife.paging.ImagePagingSource
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import com.sarftec.lessonsinlife.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepositoryImpl @ExperimentalPagingApi
@Inject constructor(
    private val pagingSource: ImagePagingSource,
) : ImageRepository {

    @ExperimentalPagingApi
    override fun getImageFlow(): Flow<PagingData<PictureQuote>> {
        return Pager(pagingConfig(),) { pagingSource }.flow
    }

    private fun pagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = ImageRepository.IMAGE_PAGE_SIZE,
            enablePlaceholders = false
        )
    }
}