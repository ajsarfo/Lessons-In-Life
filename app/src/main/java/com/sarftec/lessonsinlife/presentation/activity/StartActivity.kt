package com.sarftec.lessonsinlife.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.database.DatabaseSetup
import com.sarftec.lessonsinlife.presentation.dialog.StartPreparation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    @Inject
    lateinit var databaseSetup: DatabaseSetup

    private val preparationDialog by lazy {
        StartPreparation(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            if (!databaseSetup.isPrepared()) {
                preparationDialog.show()
                databaseSetup.prepareDatabase()
            }
            startActivity(Intent(this@StartActivity, SplashActivity::class.java))
            finish()
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
        }
    }
}