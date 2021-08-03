package com.sarftec.lessonsinlife.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutQuoteListBinding
import com.sarftec.lessonsinlife.presentation.binding.QuoteListBinding
import com.sarftec.lessonsinlife.presentation.viewmodel.QuoteListViewModel

class QuoteListViewHolder(
    private val layoutBinding: LayoutQuoteListBinding,
    private val viewModel: QuoteListViewModel,
    private val onClick: (Quote) -> Unit
) : RecyclerView.ViewHolder(layoutBinding.root) {

    fun bind(quote: Quote) {
        layoutBinding.binding = QuoteListBinding(quote, itemView.context, viewModel, onClick)
        layoutBinding.executePendingBindings()
    }
}