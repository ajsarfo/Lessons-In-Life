package com.sarftec.lessonsinlife.presentation.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutPagerViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class QuotePagerAdapter(
    private val coroutineScope: CoroutineScope,
    private var items: List<Quote> = emptyList()
) : RecyclerView.Adapter<QuotePagerViewHolder>() {

    private val fontFlow = MutableStateFlow<Typeface?>(null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotePagerViewHolder {
       val layoutBinding =  LayoutPagerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuotePagerViewHolder(layoutBinding, coroutineScope, fontFlow)
    }

    override fun onBindViewHolder(holder: QuotePagerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onViewAttachedToWindow(holder: QuotePagerViewHolder) {
        holder.subscribe()
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: QuotePagerViewHolder) {
        holder.unsubscribe()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int = items.size

    fun changeTypeface(typeface: Typeface) {
        fontFlow.value = typeface
    }

    fun submitData(items: List<Quote>) {
        this.items = items
        notifyDataSetChanged()
    }
}