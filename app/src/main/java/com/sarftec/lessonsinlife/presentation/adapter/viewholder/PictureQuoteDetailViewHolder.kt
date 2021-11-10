package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sarftec.lessonsinlife.databinding.LayoutPictureQuoteDetailBinding
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import com.sarftec.lessonsinlife.store.ImageStore

class PictureQuoteDetailViewHolder private constructor(
    private val layoutBinding: LayoutPictureQuoteDetailBinding,
    private val imageStore: ImageStore,
    private val onSelected: (PictureQuote) -> Unit
) : RecyclerView.ViewHolder(layoutBinding.root) {

    fun bind(pictureQuote: PictureQuote) {
        layoutBinding.image.load(pictureQuote.file,imageStore.imageLoader) {
            allowHardware(false)
        }
        onSelected(pictureQuote)
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            imageStore: ImageStore,
            onSelected: (PictureQuote) -> Unit
        ): PictureQuoteDetailViewHolder {
            return PictureQuoteDetailViewHolder(
                LayoutPictureQuoteDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                imageStore,
                onSelected
            )
        }
    }
}