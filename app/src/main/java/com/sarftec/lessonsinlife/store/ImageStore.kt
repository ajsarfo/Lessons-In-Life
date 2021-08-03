package com.sarftec.lessonsinlife.store

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


class ImageStore @Inject constructor(
    @ApplicationContext private val context: Context
){

    private val categoryImages = context
        .assets
        .list(CATEGORY_FOLDER)!!
        .map { it.lowercase(Locale.ENGLISH) }
        .toHashSet()

    private val backgroundImages = context
        .assets
        .list(BACKGROUND_FOLDER)!!
        .toHashSet()

    fun getNavDrawerImage() : Uri {
        return Uri.parse("file:///android_asset/navigation/drawer_image.jpg")
    }

    fun getCategoryUri(category: String) : Uri {
        val converted = category.replace(" ", "_").lowercase(Locale.ENGLISH)
        return categoryImages.firstOrNull { it.startsWith(converted) }
            ?.toUri(CATEGORY_FOLDER)
            ?: "default.jpg".toUri(CATEGORY_FOLDER)
    }

    fun randomQuoteBackground() : Uri {
        return backgroundImages.random().toUri(BACKGROUND_FOLDER)
    }

    private fun String.toUri(folder: String) : Uri {
        return Uri.parse("file:///android_asset/$folder/$this")
    }

    companion object {
        const val BACKGROUND_FOLDER = "background_images"
        const val CATEGORY_FOLDER = "category_images"
    }
}