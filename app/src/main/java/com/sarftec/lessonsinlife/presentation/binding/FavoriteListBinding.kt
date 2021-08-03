package com.sarftec.lessonsinlife.presentation.binding

import android.content.Context
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.presentation.viewmodel.FavoriteListViewModel
import com.sarftec.lessonsinlife.utils.copy
import com.sarftec.lessonsinlife.utils.share
import com.sarftec.lessonsinlife.utils.toast
import com.sarftec.lessonsinlife.utils.vibrate

class FavoriteListBinding(
    val quote: Quote,
    private val context: Context,
    private val viewModel: FavoriteListViewModel,
    private val onClick: (Quote) -> Unit,
    private val onDelete: (Quote) -> Unit
) {

    fun clicked() {
        onClick(quote)
    }

    fun copy() {
        context.apply {
            vibrate()
            toast("Copied to clipboard")
            copy(quote.message, "label")
        }
    }

    fun share() {
        context.apply {
            vibrate()
            share(quote.message, "Share")
        }
    }

    fun delete() {
        context.vibrate()
        onDelete(quote)
        viewModel.removeQuote(quote)
    }
}