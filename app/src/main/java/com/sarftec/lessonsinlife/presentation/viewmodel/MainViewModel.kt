package com.sarftec.lessonsinlife.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarftec.lessonsinlife.presentation.model.CategoryItem
import com.sarftec.lessonsinlife.repository.CategoryRepository
import com.sarftec.lessonsinlife.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories: LiveData<List<CategoryItem>>
    get() = _categories

    fun fetch() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories().map {
                CategoryItem(it, quoteRepository.getRandom(it.id).message)
            }.sortedBy { it.category.name }
        }
    }
}