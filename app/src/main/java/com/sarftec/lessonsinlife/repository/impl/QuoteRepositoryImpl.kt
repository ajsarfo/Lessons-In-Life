package com.sarftec.lessonsinlife.repository.impl

import com.sarftec.lessonsinlife.database.Database
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.repository.QuoteRepository
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val database: Database
) : QuoteRepository {

    override suspend fun fetchQuotes(categoryId: Int): List<Quote> {
       return database.quoteDao().quotes(categoryId)
    }

    override suspend fun updateFavorite(quoteId: Int, isFavorite: Boolean) {
        database.quoteDao().updateFavorite(quoteId, isFavorite)
    }

    override suspend fun getRandom(categoryId: Int): Quote {
        return database.quoteDao().randomQuote(categoryId)
    }

    override suspend fun favoriteQuotes(): List<Quote> {
        return database.quoteDao().favorites()
    }
}