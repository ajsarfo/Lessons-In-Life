package com.sarftec.lessonsinlife.presentation.viewmodel

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.*
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.presentation.activity.BaseActivity
import com.sarftec.lessonsinlife.presentation.panel.AlignmentManager
import com.sarftec.lessonsinlife.presentation.panel.BackgroundManager
import com.sarftec.lessonsinlife.presentation.panel.PanelListener
import com.sarftec.lessonsinlife.repository.QuoteRepository
import com.sarftec.lessonsinlife.store.ImageStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class QuoteAlignment { START, CENTER, END }

enum class BackgroundOption { IMAGES, GALLERY, COLOR, NEUTRAL }

class PanelState(
    var color: Int = Color.WHITE,
    var opacity: Int = -1,
    var size: Float = -1f,
    var fontLocation: String = "",
    var isAllCaps: Boolean = false,
    var isUnderlined: Boolean = false,
    var alignment: QuoteAlignment = QuoteAlignment.CENTER,
    var backgroundOption: BackgroundOption = BackgroundOption.NEUTRAL
) {
    private var changeSet: Boolean = true

    override fun equals(other: Any?): Boolean {
        if (other !is PanelState) return false
        return other.changeSet == this.changeSet
    }

    override fun hashCode(): Int {
        return if (changeSet) 1 else 0
    }

    fun switch(): PanelState {
        changeSet = !changeSet
        return this
    }
}

@Parcelize
class BackgroundState(
    var color: Int? = null,
    var image: String? = null,
) : Parcelable

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
    private val imageStore: ImageStore,
    private val stateHandle: SavedStateHandle
) : ViewModel(), PanelListener {

    private val _indexedQuotes = MutableLiveData<IndexedQuotes>()
    val indexedQuotes: LiveData<IndexedQuotes>
        get() = _indexedQuotes

    private val _currentQuote = MutableLiveData<Quote>()
    val currentQuote: LiveData<Quote>
        get() = _currentQuote

    private val _panelState = MutableLiveData(PanelState())
    val panelState: LiveData<PanelState>
        get() = _panelState

    private val _background = MutableLiveData<BackgroundState>()
    val background: LiveData<BackgroundState>
        get() = _background

    fun fetch() {
        viewModelScope.launch {
            _background.value = stateHandle.get<BackgroundState>(SAVED_IMAGE) ?: kotlin.run {
                BackgroundState(image = imageStore.randomQuoteBackground().toString()).also {
                    stateHandle.set<BackgroundState>(SAVED_IMAGE, it)
                }
            }
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

    fun neutralizeBackgroundOption() {
        _panelState.value?.let {
            it.backgroundOption = BackgroundOption.NEUTRAL
        }
    }

    fun randomBackground() {
        setBackgroundImage(imageStore.randomQuoteBackground())
    }

    fun setBackgroundColor(color: Int) {
        val changes = _background.value?.also {
            it.image = null
            it.color = color
        }
        stateHandle.set<BackgroundState>(SAVED_IMAGE, changes)
        _background.value = changes
    }

    fun setBackgroundImage(uri: Uri) {
        val changes = _background.value?.also {
            it.image = uri.toString()
            it.color = null
        }
        stateHandle.set<BackgroundState>(SAVED_IMAGE, changes)
        _background.value = changes
    }


    /*
     fun setBackgroundImage(imageHolder: ImageHolder) {
         val changes = _background.value?.also {
             it.image = imageHolder.uri.toString()
             it.color = null
             it.isImageAsset = imageHolder is ImageHolder.AssetImage
         }
         savedStateHandle.set<BackgroundState>(SAVED_IMAGE, changes)
         _background.value = changes
     }

     fun setBackgroundColor(color: Int) {
         val changes = _background.value?.also {
             it.image = null
             it.color = color
         }
         savedStateHandle.set<BackgroundState>(SAVED_IMAGE, changes)
         _background.value = changes
     }
     */

    fun setBundle(bundle: Bundle) {
        stateHandle.set("bundle", bundle)
    }

    private fun updatePanelState() {
        _panelState.value = _panelState.value?.switch()
    }

    /**
     ** This block of code implement panel listener
     **/
    override fun setFont(fontLocation: String) {
        _panelState.value?.fontLocation = fontLocation
        updatePanelState()
    }

    override fun setColor(color: Int) {
        _panelState.value?.color = color
        updatePanelState()
    }

    override fun setOpacity(color: Int) {
        _panelState.value?.opacity = color
        updatePanelState()
    }

    override fun setSize(size: Float) {
        _panelState.value?.size = size
        updatePanelState()
    }

    override fun setAlignment(position: AlignmentManager.Position) {
        _panelState.value?.alignment = when (position) {
            AlignmentManager.Position.CENTER -> QuoteAlignment.CENTER
            AlignmentManager.Position.LEFT -> QuoteAlignment.START
            AlignmentManager.Position.RIGHT -> QuoteAlignment.END
        }
        updatePanelState()
    }

    override fun setAllCaps(option: AlignmentManager.Option) {
        _panelState.value?.isAllCaps = option == AlignmentManager.Option.YES
        updatePanelState()
    }

    override fun setUnderlined(option: AlignmentManager.Option) {
        _panelState.value?.isUnderlined = option == AlignmentManager.Option.YES
        updatePanelState()
    }

    override fun chooseBackground(chooser: BackgroundManager.Chooser) {
        _panelState.value?.backgroundOption = when (chooser) {
            BackgroundManager.Chooser.IMAGE -> BackgroundOption.IMAGES
            BackgroundManager.Chooser.GALLERY -> BackgroundOption.GALLERY
            BackgroundManager.Chooser.COLOR -> BackgroundOption.COLOR
        }
        updatePanelState()
    }

    override fun getAlignment(): AlignmentManager.Position {
        if (_panelState.value == null) AlignmentManager.Position.CENTER
        return when (_panelState.value!!.alignment) {
            QuoteAlignment.CENTER -> AlignmentManager.Position.CENTER
            QuoteAlignment.START -> AlignmentManager.Position.LEFT
            QuoteAlignment.END -> AlignmentManager.Position.RIGHT
        }
    }

    override fun isAllCaps(): AlignmentManager.Option {
        return _panelState.value?.isAllCaps?.let {
            if (it) AlignmentManager.Option.YES else AlignmentManager.Option.NO
        } ?: AlignmentManager.Option.NO
    }

    override fun isUnderlined(): AlignmentManager.Option {
        return _panelState.value?.isUnderlined?.let {
            if (it) AlignmentManager.Option.YES else AlignmentManager.Option.NO
        } ?: AlignmentManager.Option.NO
    }

    companion object {
        const val PARCEL = "parcel"
        const val CURRENT_INDEX = "detail_current_index"
        const val SAVED_IMAGE = "saved_image"
    }
}