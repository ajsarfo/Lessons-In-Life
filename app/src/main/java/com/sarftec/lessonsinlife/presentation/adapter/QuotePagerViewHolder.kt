package com.sarftec.lessonsinlife.presentation.adapter

import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutPagerViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class QuotePagerViewHolder(
    private val layoutBinding: LayoutPagerViewBinding,
    private val coroutineScope: CoroutineScope,
    private val fontFlow: StateFlow<Typeface?>
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private var job: Job? = null


    fun subscribe() {
        job = coroutineScope.launch {
            fontFlow.filterNotNull().collect {
                layoutBinding.pagerText.typeface = it
            }
        }
    }

    fun unsubscribe() {
        job?.cancel()
    }

    fun bind(quote: Quote) {
        layoutBinding.pagerText.text = quote.message
    }
}