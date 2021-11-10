package com.sarftec.lessonsinlife.presentation.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.sarftec.lessonsinlife.databinding.LayoutPictureWallpaperBinding

class PictureWallpaper(
    parent: ViewGroup,
    onSet: () -> Unit
) : AlertDialog(parent.context) {

    init {
        val layoutBinding = LayoutPictureWallpaperBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            set.setOnClickListener { onSet() }
            cancel.setOnClickListener { dismiss() }
        }
        setView(layoutBinding.root)
    }
}