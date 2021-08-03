package com.sarftec.lessonsinlife.presentation.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.sarftec.lessonsinlife.databinding.LayoutPreparationProgressBinding

class StartPreparation(context: Context) : AlertDialog(context) {

    init {
        setView(
            LayoutPreparationProgressBinding.inflate(
                LayoutInflater.from(context)
            ).root
        )
    }
}