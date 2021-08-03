package com.sarftec.lessonsinlife.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.*
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.presentation.activity.BaseActivity
import com.sarftec.lessonsinlife.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteListViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val _quotes = MutableLiveData<List<Quote>>()
    val quote: LiveData<List<Quote>>
    get() = _quotes

    fun getToolbarTitle() : String? {
        return stateHandle.get<Bundle>("bundle")?.getString(BaseActivity.CATEGORY_SELECTED_NAME)?.let {
        if(!it.lowercase().trim().endsWith("quotes")) "$it Lessons" else it
        }
    }

    fun fetch() {
        viewModelScope.launch {
            stateHandle.get<Bundle>("bundle")?.let { bundle ->
                _quotes.value = quoteRepository.fetchQuotes(
                    bundle.getInt(BaseActivity.CATEGORY_SELECTED_ID)
                )
            }
        }
    }

    fun updateFavorite(quote: Quote) {
        viewModelScope.launch {
            quoteRepository.updateFavorite(quote.id, quote.isFavorite)
        }
    }

    fun setBundle(bundle: Bundle) {
        stateHandle.set("bundle", bundle)
    }
}