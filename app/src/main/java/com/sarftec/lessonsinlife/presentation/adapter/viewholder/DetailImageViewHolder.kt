package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class DetailImageViewHolder(
    private val imageView: AppCompatImageView,
    private val onClick: (Uri) -> Unit
) : RecyclerView.ViewHolder(imageView) {

    fun bind(uri: Uri) {
        imageView.load(uri)
        imageView.setOnClickListener {
            onClick(uri)
        }
    }
}