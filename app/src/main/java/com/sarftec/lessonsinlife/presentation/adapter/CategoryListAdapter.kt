package com.sarftec.lessonsinlife.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.databinding.LayoutCategoryListBinding
import com.sarftec.lessonsinlife.store.ImageStore
import com.sarftec.lessonsinlife.presentation.model.CategoryItem

class CategoryListAdapter(
    private var items: List<CategoryItem> = emptyList(),
    private val imageStore: ImageStore,
    private val callback: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val layoutBinding = LayoutCategoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryListViewHolder(layoutBinding, imageStore, callback)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
       holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submit(data: List<CategoryItem>) {
        items = data
        notifyDataSetChanged()
    }
}