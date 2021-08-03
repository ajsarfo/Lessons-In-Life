package com.sarftec.lessonsinlife.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutQuoteListBinding
import com.sarftec.lessonsinlife.presentation.viewmodel.QuoteListViewModel

class QuoteListAdapter(
    private var items: List<Quote> = emptyList(),
    private val viewModel: QuoteListViewModel,
    private val onClick: (Quote) -> Unit

) : RecyclerView.Adapter<QuoteListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteListViewHolder {
        val layoutBinding = LayoutQuoteListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuoteListViewHolder(layoutBinding, viewModel, onClick)
    }

    override fun onBindViewHolder(holder: QuoteListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun resetQuoteFavorites(indexedFavorites: Set<Map.Entry<Int, Boolean>>) {
        indexedFavorites.forEach {
            items[it.key].isFavorite = it.value
            notifyItemChanged(it.key)
        }
    }

    fun submitData(items: List<Quote>) {
        this.items = items
        notifyDataSetChanged()
    }
}