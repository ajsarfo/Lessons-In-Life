package com.sarftec.lessonsinlife.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sarftec.lessonsinlife.presentation.adapter.viewholder.PictureQuoteDetailViewHolder
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import com.sarftec.lessonsinlife.store.ImageStore

class PictureQuoteDetailAdapter(
    private val imageStore: ImageStore,
    private val onSelected: (PictureQuote) -> Unit
) : PagingDataAdapter<PictureQuote, PictureQuoteDetailViewHolder>(PictureQuoteAdapter.DiffComparator) {

    override fun onBindViewHolder(viewHolder: PictureQuoteDetailViewHolder, position: Int) {
        getItem(position)?.let {
            viewHolder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): PictureQuoteDetailViewHolder {
        return PictureQuoteDetailViewHolder.getInstance(parent, imageStore, onSelected)
    }
}
