package com.sarftec.lessonsinlife.presentation.binding

import com.sarftec.lessonsinlife.store.ImageStore
import com.sarftec.lessonsinlife.presentation.model.CategoryItem

class CategoryListBinding(
    val categoryItem: CategoryItem,
    imageStore: ImageStore,
    private val onClick: (CategoryItem) -> Unit
) {
    val uri = imageStore.getCategoryUri(categoryItem.category.name)

    fun clicked() = onClick(categoryItem)
}