package com.sarftec.lessonsinlife.presentation.activity

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.appodeal.ads.Appodeal
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.advertisement.AdCountManager
import com.sarftec.lessonsinlife.advertisement.InterstitialManager
import com.sarftec.lessonsinlife.databinding.ActivityQuoteDetailBinding
import com.sarftec.lessonsinlife.presentation.adapter.QuotePagerAdapter
import com.sarftec.lessonsinlife.presentation.viewmodel.QuoteDetailViewModel
import com.sarftec.lessonsinlife.store.FontStore
import com.sarftec.lessonsinlife.utils.copy
import com.sarftec.lessonsinlife.utils.share
import com.sarftec.lessonsinlife.utils.toast
import com.sarftec.lessonsinlife.utils.vibrate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuoteDetailActivity : BaseActivity() {

    private val binding by lazy {
        ActivityQuoteDetailBinding.inflate(layoutInflater)
    }

    private val fontStore by lazy {
        FontStore(this)
    }

    private val quotePagerAdapter by lazy {
        QuotePagerAdapter(lifecycleScope)
    }

    private val interstitialManager by lazy {
        InterstitialManager(
            this,
            networkManager,
            listOf(2)
        )
    }

    private val viewModel by viewModels<QuoteDetailViewModel>()

    override fun onBackPressed() {
        if(canShowInterstitialAd()) interstitialManager.customShowAd {
            super.onBackPressed()
        }
        else super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        Appodeal.show(this, Appodeal.BANNER_VIEW)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState ?: Appodeal.cache(this, Appodeal.INTERSTITIAL)
        Appodeal.setBannerViewId(R.id.main_banner)
        window.apply {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        setContentView(binding.root)
        savedInstanceState ?: kotlin.run {
            intent.getBundleExtra(ACTIVITY_BUNDLE)?.let {
                viewModel.setBundle(it)
            }
        }
        configureBottomView()
        configureViewPager()
        fetchRandomBackground()
        viewModel.fetch()
        viewModel.indexedQuotes.observe(this) {
            quotePagerAdapter.submitData(it.quotes)
            binding.viewPager.setCurrentItem(it.index, false)
        }
        viewModel.currentQuote.observe(this) {
            binding.vpFav.setImageResource(
                if(it.isFavorite) R.drawable.ic_love_filled else R.drawable.ic_love_borderless
            )
        }
    }

    private fun configureViewPager() {
        binding.viewPager.adapter = quotePagerAdapter
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.setCurrentQuoteIndex(position)
                    super.onPageSelected(position)
                }
            }
        )
    }

    private fun fetchRandomBackground() {
        vibrate()
        binding.background.load(imageStore.randomQuoteBackground())
    }

    private fun configureBottomView() {
        binding.vpBg.setOnClickListener {
            vibrate()
           fetchRandomBackground()
        }

        binding.vpFav.setOnClickListener { _ ->
            viewModel.changeCurrentQuoteFavorite()?.let {
                vibrate()
                toast(if(it.isFavorite) "Added to Favorites" else "Removed from favorites")
            }
        }

        binding.vpFont.setOnClickListener {
            vibrate()
            quotePagerAdapter.changeTypeface(fontStore.randomFont())
        }

        binding.vpShare.setOnClickListener { _ ->
           viewModel.getCurrentQuote()?.let {
               vibrate()
               share(it.message, "Share")
           }
        }

        binding.vpCopy.setOnClickListener { _ ->
           viewModel.getCurrentQuote()?.let {
               vibrate()
               toast("Copied to clipboard")
               copy(it.message, "label")
           }
        }
    }

    companion object {
        private val addCounter = AdCountManager(listOf(2, 5))

        fun canShowInterstitialAd() : Boolean {
            return addCounter.canShow()
        }
    }
}