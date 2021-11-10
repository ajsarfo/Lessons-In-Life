package com.sarftec.lessonsinlife.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sarftec.lessonsinlife.presentation.adapter.viewholder.PictureQuoteViewHolder
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import com.sarftec.lessonsinlife.store.ImageStore

class PictureQuoteAdapter(
private val imageStore: ImageStore,
private val onClick: (PictureQuote) -> Unit
) : PagingDataAdapter<PictureQuote, PictureQuoteViewHolder>(DiffComparator){

    override fun onBindViewHolder(viewHolder: PictureQuoteViewHolder, position: Int) {
        getItem(position)?.let {
            viewHolder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): PictureQuoteViewHolder {
       return PictureQuoteViewHolder.getInstance(parent, imageStore, onClick)
    }

    object DiffComparator : DiffUtil.ItemCallback<PictureQuote>() {

        override fun areItemsTheSame(oldItem: PictureQuote, newItem: PictureQuote): Boolean {
          return oldItem.file.name == newItem.file.name
        }

        override fun areContentsTheSame(oldItem: PictureQuote, newItem: PictureQuote): Boolean {
            return oldItem.file.name == newItem.file.name
        }
    }
}