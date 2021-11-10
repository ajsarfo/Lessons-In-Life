package com.sarftec.lessonsinlife.database.model

import com.sarftec.lessonsinlife.presentation.model.CategoryItem

sealed class CategoryUI(val id: Int) {
    class Model(val categoryItem: CategoryItem) : CategoryUI(MODEL_ID)
    object Separator : CategoryUI(SEPARATOR_ID)

    companion object {
        const val MODEL_ID = 0
        const val SEPARATOR_ID = 1
    }
}