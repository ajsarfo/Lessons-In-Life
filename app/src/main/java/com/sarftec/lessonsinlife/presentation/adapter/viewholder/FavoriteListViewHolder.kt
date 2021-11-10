package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutFavoriteListBinding
import com.sarftec.lessonsinlife.presentation.binding.FavoriteListBinding
import com.sarftec.lessonsinlife.presentation.viewmodel.FavoriteListViewModel

class FavoriteListViewHolder(
    private val layoutBinding: LayoutFavoriteListBinding,
    private val viewModel: FavoriteListViewModel,
    private val onClick: (Quote) -> Unit,
    private val onDelete: (Quote) -> Unit
) : RecyclerView.ViewHolder(layoutBinding.root) {

    fun bind(quote: Quote) {
        layoutBinding.binding = FavoriteListBinding(quote, itemView.context, viewModel, onClick, onDelete)
        layoutBinding.executePendingBindings()
    }
}