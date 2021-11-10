package com.sarftec.lessonsinlife.presentation.adapter.viewholder

import android.graphics.Paint
import android.graphics.Typeface
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutPagerViewBinding
import com.sarftec.lessonsinlife.presentation.adapter.QuotePagerAdapter
import com.sarftec.lessonsinlife.presentation.viewmodel.PanelState
import com.sarftec.lessonsinlife.presentation.viewmodel.QuoteAlignment

class QuotePagerViewHolder(
    private val layoutBinding: LayoutPagerViewBinding,
    private val onClick: () -> Unit
) : RecyclerView.ViewHolder(layoutBinding.root), QuotePagerAdapter.PagerHolderListener {

    init {
        layoutBinding.clickView.setOnClickListener {
            onClick()
        }
    }
    fun bind(quote: Quote) {
        layoutBinding.pagerText.text = quote.message
    }

    override fun notifyPanelStateChanged(state: PanelState) {
        with(layoutBinding.pagerText) {
            setTextColor(state.color)
            if(state.size != -1f) textSize = state.size
            if(state.fontLocation.isNotEmpty()) typeface = Typeface.createFromAsset(itemView.context.assets, state.fontLocation)
            gravity = when(state.alignment) {
                QuoteAlignment.END -> Gravity.END
                QuoteAlignment.START ->  Gravity.START
                QuoteAlignment.CENTER -> Gravity.CENTER
            }
            isAllCaps = state.isAllCaps
            paintFlags = if (state.isUnderlined) Paint.UNDERLINE_TEXT_FLAG
            else paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }
}