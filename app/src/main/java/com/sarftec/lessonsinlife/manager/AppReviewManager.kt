package com.sarftec.lessonsinlife.manager

import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.sarftec.lessonsinlife.utils.readSettings
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppReviewManager(
    private val activity: AppCompatActivity
) {
    private val reviewManager = ReviewManagerFactory.create(activity)
    private val reviewStateFlow = MutableStateFlow<ReviewInfo?>(null)

    private var job: Job? = null

    fun init() {
        job = activity.lifecycleScope.launch {
            reviewStateFlow.collect {
                it?.let {
                    reviewManager.launchReviewFlow(activity, it)
                    throw CancellationException()
                }
            }
        }
    }

    suspend fun triggerReview() {
        activity.readSettings(App_START_UP_TIMES, 0).first().let {
            if(it >= 3) review() else job?.cancel()
        }
    }

    private fun review() {
        reviewManager.requestReviewFlow().let {
            it.addOnCompleteListener { request ->
                if (request.isSuccessful) {
                    reviewStateFlow.value = request.result
                }
            }
        }
    }

    companion object {
        val App_START_UP_TIMES = intPreferencesKey("App_start_up")
    }
}