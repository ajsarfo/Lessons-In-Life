package com.sarftec.lessonsinlife.repository

import com.sarftec.lessonsinlife.repository.impl.CategoryRepositoryImpl
import com.sarftec.lessonsinlife.repository.impl.QuoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Injection {

    @Binds
    @Singleton
    abstract fun quoteRepository(repository: QuoteRepositoryImpl) : QuoteRepository

    @Binds
    @Singleton
    abstract fun categoryRepository(repositoryImpl: CategoryRepositoryImpl) : CategoryRepository
}