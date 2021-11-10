package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sarftec.lessonsinlife.database.model.CategoryUI
import com.sarftec.lessonsinlife.databinding.LayoutCategoryListBinding
import com.sarftec.lessonsinlife.presentation.binding.CategoryListBinding
import com.sarftec.lessonsinlife.presentation.model.CategoryItem
import com.sarftec.lessonsinlife.store.ImageStore

class CategoryListItemViewHolder(
    private val layoutBinding: LayoutCategoryListBinding,
    private val imageStore: ImageStore,
    private val callback: (CategoryItem) -> Unit
) : CategoryListViewHolder(layoutBinding.root) {

    override fun bind(categoryUI: CategoryUI) {
        if(categoryUI !is CategoryUI.Model) return
        layoutBinding.binding = CategoryListBinding(
            categoryUI.categoryItem,
            imageStore,
            callback
        )
        layoutBinding.executePendingBindings()
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            imageStore: ImageStore,
            callback: (CategoryItem) -> Unit
        ): CategoryListViewHolder {
            val layoutBinding = LayoutCategoryListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CategoryListItemViewHolder(layoutBinding, imageStore, callback)
        }
    }
}