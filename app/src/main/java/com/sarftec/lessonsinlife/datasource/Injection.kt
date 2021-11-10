package com.sarftec.lessonsinlife.datasource

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface Injection {

    @Binds
    fun imageDataSource(source: FirebaseImageDataSource) : ImageDataSource
}