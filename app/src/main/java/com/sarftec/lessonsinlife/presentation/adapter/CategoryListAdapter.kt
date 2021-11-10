package com.sarftec.lessonsinlife.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sarftec.lessonsinlife.database.model.CategoryUI
import com.sarftec.lessonsinlife.presentation.adapter.viewholder.CategoryListItemViewHolder
import com.sarftec.lessonsinlife.presentation.adapter.viewholder.CategoryListSeparatorViewHolder
import com.sarftec.lessonsinlife.presentation.adapter.viewholder.CategoryListViewHolder
import com.sarftec.lessonsinlife.presentation.model.CategoryItem
import com.sarftec.lessonsinlife.store.ImageStore

class CategoryListAdapter(
    private val imageStore: ImageStore,
    private val pictureQuoteAdapter: PictureQuoteAdapter,
    private val callback: (CategoryItem) -> Unit
) : PagingDataAdapter<CategoryUI, CategoryListViewHolder>(DiffComparator) {

    override fun onBindViewHolder(viewHolder: CategoryListViewHolder, position: Int) {
       getItem(position)?.let {
           viewHolder.bind(it)
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): CategoryListViewHolder {
        return if(type == SEPARATOR_ITEM)
            CategoryListSeparatorViewHolder.getInstance(parent, pictureQuoteAdapter)
        else CategoryListItemViewHolder.getInstance(
            parent,
            imageStore,
            callback
        )
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let {
            if(it is CategoryUI.Separator) SEPARATOR_ITEM else MODEL_ITEM
        } ?: MODEL_ITEM
    }

    companion object {
        const val SEPARATOR_ITEM = 0
        const val MODEL_ITEM = 1
    }

    object DiffComparator : DiffUtil.ItemCallback<CategoryUI>() {
        override fun areItemsTheSame(oldItem: CategoryUI, newItem: CategoryUI): Boolean {
            return when {
                oldItem.id != newItem.id -> false
                oldItem is CategoryUI.Model && newItem is CategoryUI.Model -> {
                    oldItem.categoryItem.category.id == newItem.categoryItem.category.id
                }
                else -> true
            }
        }

        override fun areContentsTheSame(oldItem: CategoryUI, newItem: CategoryUI): Boolean {
            return when {
                oldItem.id != newItem.id -> false
                oldItem is CategoryUI.Model && newItem is CategoryUI.Model -> {
                    oldItem.categoryItem.category.name == newItem.categoryItem.category.name
                }
                else -> true
            }
        }
    }

    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {

    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
       holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submit(data: List<CategoryItem>) {
        items = data
        notifyDataSetChanged()
    }
     */
}