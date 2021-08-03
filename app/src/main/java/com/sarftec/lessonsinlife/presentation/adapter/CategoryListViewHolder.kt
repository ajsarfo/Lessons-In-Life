package com.sarftec.lessonsinlife.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.databinding.LayoutCategoryListBinding
import com.sarftec.lessonsinlife.store.ImageStore
import com.sarftec.lessonsinlife.presentation.binding.CategoryListBinding
import com.sarftec.lessonsinlife.presentation.model.CategoryItem

class CategoryListViewHolder(
    private val layoutBinding: LayoutCategoryListBinding,
    private val imageStore: ImageStore,
    private val callback: (CategoryItem) -> Unit
) : RecyclerView.ViewHolder(layoutBinding.root) {

    fun bind(categoryItem: CategoryItem) {
        layoutBinding.binding = CategoryListBinding(
            categoryItem,
            imageStore,
            callback
        )
        layoutBinding.executePendingBindings()
    }
}