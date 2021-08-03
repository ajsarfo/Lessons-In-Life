package com.sarftec.lessonsinlife.store

import android.content.Context
import android.graphics.Typeface

class FontStore(private val context: Context) {

    private val fonts = context
        .assets
        .list("fonts")!!.map {
            Typeface.createFromAsset(context.assets, "fonts/$it")
        }.toHashSet()

    fun randomFont() : Typeface = fonts.random()
}