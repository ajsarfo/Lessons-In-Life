package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.CategoryUI

abstract class CategoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(categoryUI: CategoryUI)
}