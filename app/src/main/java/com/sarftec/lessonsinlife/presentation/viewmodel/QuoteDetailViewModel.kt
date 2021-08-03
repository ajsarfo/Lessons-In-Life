package com.sarftec.lessonsinlife.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.*
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.presentation.activity.BaseActivity
import com.sarftec.lessonsinlife.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class IndexedQuotes(
    var index: Int,
    val quotes: List<Quote>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is IndexedQuotes) return false
        return other.index == index
    }

    override fun hashCode(): Int {
        var result = index
        result = 31 * result + quotes.hashCode()
        return result
    }
}

@HiltViewModel
class QuoteDetailViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val _indexedQuotes = MutableLiveData<IndexedQuotes>()
    val indexedQuotes: LiveData<IndexedQuotes>
        get() = _indexedQuotes

    private val _currentQuote = MutableLiveData<Quote>()
    val currentQuote: LiveData<Quote>
        get() = _currentQuote

    fun fetch() {
        viewModelScope.launch {
            stateHandle.get<Bundle>("bundle")?.let { bundle ->
                bundle.getString(BaseActivity.NAVIGATION_ROOT)?.let {
                    val quoteList = if (it == BaseActivity.NAVIGATION_QUOTE_LIST) {
                        quoteRepository.fetchQuotes(bundle.getInt(BaseActivity.CATEGORY_SELECTED_ID))
                    } else {
                        quoteRepository.favoriteQuotes()
                    }
                    var index = stateHandle.get<Int>(CURRENT_INDEX) ?: -1
                    if (index == -1) {
                        index = quoteList.indexOfFirst { quote ->
                            quote.id == bundle.getInt(BaseActivity.QUOTE_SELECTED_ID)
                        }
                    }
                    _indexedQuotes.value = IndexedQuotes(index, quoteList)
                    setCurrentQuoteIndex(index)
                }
            }
        }
    }

    fun getCurrentQuote(): Quote? {
        val index = stateHandle.get<Int>(CURRENT_INDEX) ?: -1
        if (index == -1) return null
        return _indexedQuotes.value?.quotes?.get(index)
    }


    fun setCurrentQuoteIndex(index: Int) {
        stateHandle.set(CURRENT_INDEX, index)
        _indexedQuotes.value?.let {
            _currentQuote.value = it.quotes[index]
        }
    }

    fun changeCurrentQuoteFavorite(): Quote? {
        val current = _currentQuote.value ?: return null
        current.isFavorite = !current.isFavorite
        stateHandle.get<Int>(CURRENT_INDEX)?.let { index ->
            BaseActivity.modifiedQuoteList
                .entries
                .firstOrNull { it.key == index }
                ?.setValue(current.isFavorite) ?: kotlin.run {
                BaseActivity.modifiedQuoteList.put(index, current.isFavorite)
            }
        }
        viewModelScope.launch {
            quoteRepository.updateFavorite(current.id, current.isFavorite)
        }
        _currentQuote.value = current
        return current
    }

    fun setBundle(bundle: Bundle) {
        stateHandle.set("bundle", bundle)
    }

    companion object {
        const val CURRENT_INDEX = "detail_current_index"
    }
}