package com.sarftec.lessonsinlife.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.advertisement.AdCountManager
import com.sarftec.lessonsinlife.advertisement.BannerManager
import com.sarftec.lessonsinlife.databinding.ActivityFavoriteListBinding
import com.sarftec.lessonsinlife.presentation.adapter.FavoriteListAdapter
import com.sarftec.lessonsinlife.presentation.adapter.ItemDecorator
import com.sarftec.lessonsinlife.presentation.viewmodel.FavoriteListViewModel
import com.sarftec.lessonsinlife.utils.vibrate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : BaseActivity() {

    private val viewModel by viewModels<FavoriteListViewModel>()

    private val binding by lazy {
        ActivityFavoriteListBinding.inflate(layoutInflater)
    }

    private val listAdapter by lazy {
        FavoriteListAdapter(viewModel = viewModel) { quote ->
            vibrate()
            interstitialManager?.showAd {
                navigateToWithBundle(
                    QuoteDetailActivity::class.java,
                    bundle = Bundle().apply {
                        putInt(CATEGORY_SELECTED_ID, quote.categoryId)
                        putInt(QUOTE_SELECTED_ID, quote.id)
                        putString(NAVIGATION_ROOT, NAVIGATION_FAVORITE_LIST)
                    }
                )
            }
        }
    }

    override fun createAdCounterManager(): AdCountManager {
        return AdCountManager(listOf(3,2))
    }
    override fun onResume() {
        super.onResume()
        viewModel.resetQuoteFavorites()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_favorite),
            binding.mainBanner
        )
        /**********************************************************/
        viewModel.fetch()
        binding.toolbar.setNavigationOnClickListener {
            vibrate()
            onBackPressed()
        }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            addItemDecoration(ItemDecorator(16f, this@FavoriteActivity))
            adapter = listAdapter
        }
        viewModel.quote.observe(this) {
            if(it.isEmpty()) binding.noFavorite.visibility = View.VISIBLE
            listAdapter.submitData(it)
        }
    }
}