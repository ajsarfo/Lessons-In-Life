package com.sarftec.lessonsinlife.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.database.DatabaseSetup
import com.sarftec.lessonsinlife.presentation.dialog.StartPreparation
import com.sarftec.lessonsinlife.utils.IS_DARK_MODE
import com.sarftec.lessonsinlife.utils.readSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
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
            switchNightMode()
            startActivity(Intent(this@StartActivity, SplashActivity::class.java))
            finish()
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
        }
    }

    private suspend fun switchNightMode() {
        val constant =
            if (readSettings(IS_DARK_MODE, false).first()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(constant)
    }
}