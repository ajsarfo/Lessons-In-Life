package com.sarftec.lessonsinlife.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.databinding.LayoutDetailImageBinding
import com.sarftec.lessonsinlife.presentation.adapter.viewholder.DetailImageViewHolder
import com.sarftec.lessonsinlife.store.ImageStore

class DetailImageAdapter(
    private val imageStore: ImageStore,
    private val onClick: (Uri) -> Unit
) : RecyclerView.Adapter<DetailImageViewHolder>() {

    private val items by lazy {
        imageStore.getQuoteImages()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImageViewHolder {
        val layoutBinding = LayoutDetailImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailImageViewHolder(layoutBinding.root, onClick)
    }

    override fun onBindViewHolder(holder: DetailImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}