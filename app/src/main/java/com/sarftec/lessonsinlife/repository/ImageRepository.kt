package com.sarftec.lessonsinlife.repository

import androidx.paging.PagingData
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
  fun getImageFlow() : Flow<PagingData<PictureQuote>>

  companion object {
    const val IMAGE_PAGE_SIZE = 1
  }
}