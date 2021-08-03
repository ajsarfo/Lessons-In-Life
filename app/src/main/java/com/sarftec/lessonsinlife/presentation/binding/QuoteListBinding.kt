package com.sarftec.lessonsinlife.presentation.binding

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.BR
import com.sarftec.lessonsinlife.database.model.Quote
import com.sarftec.lessonsinlife.presentation.viewmodel.QuoteListViewModel
import com.sarftec.lessonsinlife.utils.*

class QuoteListBinding(
    val quote: Quote,
    private val context: Context,
    private val viewModel: QuoteListViewModel,
    private val onClick: (Quote) -> Unit
) : BaseObservable() {

    @get:Bindable
    var favoriteIcon by bindable(retrieveFavoriteIcon(), BR.favoriteIcon)

    private fun retrieveFavoriteIcon() : Int {
        return if(quote.isFavorite) R.drawable.ic_love_filled else R.drawable.ic_love_borderless
    }

    fun clicked() = onClick(quote)

    fun copy() {
        context.apply {
            vibrate()
            toast("Copied to clipboard")
            copy(quote.message, "label")
        }
    }

    fun favorite() {
        quote.isFavorite = !quote.isFavorite
        favoriteIcon = retrieveFavoriteIcon()
        viewModel.updateFavorite(quote)
        context.apply {
            vibrate()
            toast(if(quote.isFavorite) "Added to Favorites" else "Removed from favorites")
        }
    }

    fun share() {
        context.apply {
            vibrate()
            share(quote.message, "Share")
        }
    }
}