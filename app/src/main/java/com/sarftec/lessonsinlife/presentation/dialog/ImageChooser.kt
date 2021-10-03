package com.sarftec.lessonsinlife.presentation.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.sarftec.lessonsinlife.databinding.LayoutDetailImageDialogBinding
import com.sarftec.lessonsinlife.presentation.adapter.DetailImageAdapter

class ImageChooser(
    parent: ViewGroup,
    imageAdapter: DetailImageAdapter,
) : AlertDialog(parent.context) {

    init {
        val layoutBinding = LayoutDetailImageDialogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            imageRecycler.apply {
                layoutManager = GridLayoutManager(parent.context, 3)
                setHasFixedSize(true)
                adapter = imageAdapter
            }
        }
        setView(layoutBinding.root)
    }
}