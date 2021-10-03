package com.sarftec.lessonsinlife.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.databinding.LayoutPagerViewBinding
import com.sarftec.lessonsinlife.presentation.viewmodel.PanelState

class QuotePagerAdapter(
    private var items: List<Quote> = emptyList(),
    private val onClick: () -> Unit,
    ) : RecyclerView.Adapter<QuotePagerViewHolder>() {

    private val panelStateListeners = hashSetOf<QuotePagerViewHolder>()

    private var panelState: PanelState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotePagerViewHolder {
       val layoutBinding =  LayoutPagerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuotePagerViewHolder(layoutBinding, onClick).also {
            panelStateListeners.add(it)
        }
    }

    override fun onBindViewHolder(holder: QuotePagerViewHolder, position: Int) {
        holder.bind(items[position])
        panelState?.let {
            holder.notifyPanelStateChanged(it)
        }
    }

    override fun getItemCount(): Int = items.size

    fun changePanelState(state: PanelState) {
        panelState = state
        panelStateListeners.forEach {
            it.notifyPanelStateChanged(state)
        }
    }

    fun submitData(items: List<Quote>) {
        this.items = items
        notifyDataSetChanged()
    }

    interface PagerHolderListener {
        fun notifyPanelStateChanged(state: PanelState)
    }
}