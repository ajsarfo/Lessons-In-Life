package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarftec.lessonsinlife.database.model.CategoryUI
import com.sarftec.lessonsinlife.databinding.LayoutListPictureQuoteRecyclerBinding
import com.sarftec.lessonsinlife.presentation.adapter.PictureQuoteAdapter

class CategoryListSeparatorViewHolder(
    private val layoutBinding: LayoutListPictureQuoteRecyclerBinding,
    private val pictureQuoteAdapter: PictureQuoteAdapter
) : CategoryListViewHolder(layoutBinding.root) {

    companion object {
        fun getInstance(
            parent: ViewGroup,
            quoteAdapter: PictureQuoteAdapter
        ): CategoryListSeparatorViewHolder {
            return CategoryListSeparatorViewHolder(
                LayoutListPictureQuoteRecyclerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                quoteAdapter
            )
        }
    }

    override fun bind(categoryUI: CategoryUI) {
        //Do something here later in the future
        layoutBinding.pictureRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = pictureQuoteAdapter
        }
        layoutBinding.pictureRecyclerView.visibility = View.VISIBLE
       /*
        pictureQuoteAdapter.addLoadStateListener {
            if(it.refresh != LoadState.Loading) {
                layoutBinding.pictureRecyclerView.visibility = View.VISIBLE
            }
        }
        */
    }
}