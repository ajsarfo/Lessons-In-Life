package com.sarftec.lessonsinlife.datasource

import com.sarftec.lessonsinlife.presentation.model.PictureQuote

interface ImageDataSource {
    suspend fun getInitialKey() : Int
    suspend fun getKeySize() : Result<Int>
    suspend fun getImageFile(key: Int) : Result<PictureQuote>
}