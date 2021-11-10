package com.sarftec.lessonsinlife.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.insertSeparators
import androidx.paging.map
import com.sarftec.lessonsinlife.database.model.CategoryUI
import com.sarftec.lessonsinlife.presentation.model.CategoryItem
import com.sarftec.lessonsinlife.repository.CategoryRepository
import com.sarftec.lessonsinlife.repository.ImageRepository
import com.sarftec.lessonsinlife.repository.QuoteRepository
import com.sarftec.lessonsinlife.repository.impl.ImageRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val quoteRepository: QuoteRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories: LiveData<List<CategoryItem>>
    get() = _categories

    @ExperimentalPagingApi
    val pictureImageFlow = imageRepository.getImageFlow()

    val categoryUIFlow = categoryRepository.getCategoryFlow().map { pagingData ->
        pagingData.map {
            CategoryUI.Model(
                CategoryItem(it, quoteRepository.getRandom(it.id).message)
            )
        }.insertSeparators { before: CategoryUI.Model?, after: CategoryUI? ->
            if(before == null && after != null) CategoryUI.Separator
            else null
        }
    }

    fun fetch() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories().map {
                CategoryItem(it, quoteRepository.getRandom(it.id).message)
            }.sortedBy { it.category.name }
        }
    }
}