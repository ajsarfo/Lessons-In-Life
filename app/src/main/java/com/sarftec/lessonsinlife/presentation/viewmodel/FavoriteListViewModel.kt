package com.sarftec.lessonsinlife.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.presentation.activity.BaseActivity
import com.sarftec.lessonsinlife.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private val _quotes = MutableLiveData<MutableList<Quote>>()
    val quote: LiveData<MutableList<Quote>>
        get() = _quotes

    fun fetch() {
        viewModelScope.launch {
            _quotes.value = quoteRepository.favoriteQuotes().toMutableList()
        }
    }

    fun resetQuoteFavorites() {
        viewModelScope.launch {
            val pairs = BaseActivity.modifiedQuoteList.entries
            _quotes.value?.let { quotes ->
                pairs.forEach {
                    quoteRepository.updateFavorite(quotes[it.key].id, it.value)
                }
            }
            if (pairs.isNotEmpty()) _quotes.value = quoteRepository.favoriteQuotes().toMutableList()
            pairs.clear()
        }
    }

    fun removeQuote(quote: Quote) {
        viewModelScope.launch {
            quote.isFavorite = !quote.isFavorite
            quoteRepository.updateFavorite(quote.id, quote.isFavorite)
        }
        _quotes.value?.remove(quote)
    }
}