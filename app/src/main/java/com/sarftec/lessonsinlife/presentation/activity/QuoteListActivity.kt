package com.sarftec.lessonsinlife.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.advertisement.BannerManager
import com.sarftec.lessonsinlife.databinding.ActivityQuoteListBinding
import com.sarftec.lessonsinlife.presentation.adapter.ItemDecorator
import com.sarftec.lessonsinlife.presentation.adapter.QuoteListAdapter
import com.sarftec.lessonsinlife.presentation.dialog.LoadingScreen
import com.sarftec.lessonsinlife.presentation.viewmodel.QuoteListViewModel
import com.sarftec.lessonsinlife.utils.vibrate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuoteListActivity : BaseActivity() {

    private val viewModel by viewModels<QuoteListViewModel>()

    private val binding by lazy {
        ActivityQuoteListBinding.inflate(layoutInflater)
    }

    private val listAdapter by lazy {
        QuoteListAdapter(viewModel = viewModel) { quote ->
            vibrate()
            navigateToWithBundle(
                QuoteDetailActivity::class.java,
                bundle = Bundle().apply {
                    putInt(CATEGORY_SELECTED_ID, quote.categoryId)
                    putInt(QUOTE_SELECTED_ID, quote.id)
                    putString(NAVIGATION_ROOT, NAVIGATION_QUOTE_LIST)
                }
            )
        }
    }

    private val loadingScreen by lazy {
        LoadingScreen(this)
    }

    override fun canShowInterstitial(): Boolean = false

    override fun onResume() {
        super.onResume()
        listAdapter.resetQuoteFavorites(modifiedQuoteList.entries)
        modifiedQuoteList.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_list),
            binding.mainBanner
        )
        /**********************************************************/
        savedInstanceState?.let {
            loadingScreen.show()
        }
        savedInstanceState ?: kotlin.run {
            intent.getBundleExtra(ACTIVITY_BUNDLE)?.let {
                viewModel.setBundle(it)
            }
        }
        configureToolbar()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@QuoteListActivity)
            addItemDecoration(ItemDecorator(16f, this@QuoteListActivity))
            adapter = listAdapter
        }
        viewModel.fetch()
        viewModel.quote.observe(this) {
            loadingScreen.dismiss()
            listAdapter.submitData(it)
        }
    }

    private fun configureToolbar() {
        binding.toolbar.title = viewModel.getToolbarTitle()
        binding.toolbar.setNavigationOnClickListener {
            vibrate()
            onBackPressed()
        }
    }
}