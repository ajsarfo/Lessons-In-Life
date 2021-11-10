package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sarftec.lessonsinlife.databinding.LayoutListPictureQuoteBinding
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import com.sarftec.lessonsinlife.store.ImageStore

class PictureQuoteViewHolder private constructor(
    private val layoutBinding: LayoutListPictureQuoteBinding,
    private val imageStore: ImageStore,
    private val onClick: (PictureQuote) -> Unit
) : RecyclerView.ViewHolder(layoutBinding.root) {

    fun bind(pictureQuote: PictureQuote) {
        layoutBinding.pictureImage.load(pictureQuote.file, imageStore.imageLoader)
        layoutBinding.pictureImage.setOnClickListener {
            onClick(pictureQuote)
        }
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            imageStore: ImageStore,
            onClick: (PictureQuote) -> Unit
        ): PictureQuoteViewHolder {
            return PictureQuoteViewHolder(
                LayoutListPictureQuoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                imageStore,
                onClick
            )
        }
    }
}