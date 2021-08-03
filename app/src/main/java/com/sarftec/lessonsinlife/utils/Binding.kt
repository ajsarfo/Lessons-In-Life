package com.sarftec.lessonsinlife.utils

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import coil.load
import com.sarftec.lessonsinlife.R
import kotlin.reflect.KProperty

class Bindable<T : Any>(private var value: T, private val tag: Int) {
    operator fun <U : BaseObservable> getValue(ref: U, property: KProperty<*>): T = value
    operator fun <U : BaseObservable> setValue(ref: U, property: KProperty<*>, newValue: T) {
        value = newValue
        ref.notifyPropertyChanged(tag)
    }
}

fun <T : Any> bindable(value: T, tag: Int): Bindable<T> = Bindable(value, tag)

@BindingAdapter("uri")
fun setImage(imageView: ImageView, uri: Uri) {
    imageView.load(uri) {
        placeholder(R.drawable.loading)
    }
}

@BindingAdapter("drawable")
fun setDrawable(imageView: ImageView, @DrawableRes id: Int) {
   imageView.setImageResource(id)
}