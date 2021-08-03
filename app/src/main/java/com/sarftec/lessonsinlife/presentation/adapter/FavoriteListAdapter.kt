package com.sarftec.lessonsinlife.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutFavoriteListBinding
import com.sarftec.lessonsinlife.presentation.viewmodel.FavoriteListViewModel

class FavoriteListAdapter(
    private var items: List<Quote> = emptyList(),
    private val viewModel: FavoriteListViewModel,
    private val onClick: (Quote) -> Unit
) : RecyclerView.Adapter<FavoriteListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val layoutBinding = LayoutFavoriteListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteListViewHolder(layoutBinding, viewModel, onClick) { quote ->
            val index = items.indexOfFirst { quote.id == it.id }
            if(index != -1) {
                notifyItemRemoved(index)
            }
        }
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(items: List<Quote>) {
        this.items = items
        notifyDataSetChanged()
    }
}