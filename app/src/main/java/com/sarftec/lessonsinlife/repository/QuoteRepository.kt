package com.sarftec.lessonsinlife.repository

import com.sarftec.lessonsinlife.database.model.Quote

interface QuoteRepository {
    suspend fun fetchQuotes(categoryId: Int) : List<Quote>
    suspend fun updateFavorite(quoteId: Int, isFavorite: Boolean)
    suspend fun getRandom(categoryId: Int) : Quote
    suspend fun favoriteQuotes() : List<Quote>
}