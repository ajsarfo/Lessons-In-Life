package com.sarftec.lessonsinlife.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sarftec.lessonsinlife.database.QUOTE_TABLE_NAME
import com.sarftec.lessonsinlife.database.model.Quote

@Dao
interface QuoteDao {

    @Query("select * from $QUOTE_TABLE_NAME where categoryId = :categoryId")
    suspend fun quotes(categoryId: Int) : List<Quote>

    @Query("select * from $QUOTE_TABLE_NAME where isFavorite = 1")
    suspend fun favorites() : List<Quote>

    @Query("select * from $QUOTE_TABLE_NAME where categoryId = :categoryId order by random() limit 1")
    suspend fun randomQuote(categoryId: Int) : Quote

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quotes: List<Quote>)

    @Query("update $QUOTE_TABLE_NAME set isFavorite = :isFavorite where id = :quoteId")
    suspend fun updateFavorite(quoteId: Int, isFavorite: Boolean)
}