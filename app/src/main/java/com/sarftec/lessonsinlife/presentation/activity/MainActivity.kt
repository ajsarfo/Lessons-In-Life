package com.sarftec.lessonsinlife.presentation.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.appodeal.ads.Appodeal
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.advertisement.InterstitialManager
import com.sarftec.lessonsinlife.databinding.ActivityMainBinding
import com.sarftec.lessonsinlife.manager.AppReviewManager
import com.sarftec.lessonsinlife.presentation.adapter.CategoryListAdapter
import com.sarftec.lessonsinlife.presentation.dialog.LoadingScreen
import com.sarftec.lessonsinlife.presentation.model.CategoryItem
import com.sarftec.lessonsinlife.presentation.viewmodel.MainViewModel
import com.sarftec.lessonsinlife.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val toggle by lazy {
        ActionBarDrawerToggle(
            this,
            binding.navDrawer,
            R.string.open_drawer,
            R.string.close_drawer
        )
    }
    private val loadingScreen by lazy {
        LoadingScreen(this)
    }

    private val interstitialManager by lazy {
        InterstitialManager(
            this,
            networkManager,
            listOf(1, 3, 4, 3)
        )
    }

    private val listAdapter by lazy {
        CategoryListAdapter(imageStore = imageStore) { categoryItem ->
            vibrate()
           navigateToQuoteList(categoryItem)
        }
    }

    private var drawerCallback: (() -> Unit)? = null

    override fun onStart() {
        super.onStart()
        Appodeal.show(this, Appodeal.BANNER_VIEW)
    }

    private fun navigateToQuoteList(categoryItem: CategoryItem) {
        interstitialManager.showAd {
            navigateTo(
                QuoteListActivity::class.java,
                bundle = Bundle().apply {
                    putInt(CATEGORY_SELECTED_ID, categoryItem.category.id)
                    putString(CATEGORY_SELECTED_NAME, categoryItem.category.name)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /************Appodeal Configuration*************/
        Appodeal.setBannerViewId(R.id.main_banner)
        Appodeal.initialize(
            this,
            getString(R.string.appodeal_id),
            Appodeal.BANNER_VIEW or Appodeal.INTERSTITIAL
        )
        Appodeal.cache(this, Appodeal.INTERSTITIAL)
        /***********************************************/
        lifecycleScope.launchWhenCreated {
            savedInstanceState ?: editSettings(
                AppReviewManager.App_START_UP_TIMES,
                readSettings(AppReviewManager.App_START_UP_TIMES, 0).first() + 1
            )
        }
        savedInstanceState?.let {
            loadingScreen.show()
        }
        setContentView(binding.root)
        statusColor(ContextCompat.getColor(this, R.color.main_status))
        setStatusBarBackgroundLight()
        setSupportActionBar(binding.toolbar)
        setupNavigationDrawer()
        setupNavigationView()
        viewModel.fetch()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = listAdapter
        }
        viewModel.categories.observe(this@MainActivity) {
            loadingScreen.dismiss()
            listAdapter.submit(it)
        }
        with(AppReviewManager(this@MainActivity)) {
            init()
            lifecycleScope.launchWhenCreated {
                triggerReview()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.rate) rateApp()
        return toggle.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.navDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.navDrawer.closeDrawer(GravityCompat.START)
        } else finish()
    }

    fun setupNavigationView() {
        fun onDrawerCallback(callback: () -> Unit) {
            vibrate()
            drawerCallback = callback
            binding.navDrawer.closeDrawer(GravityCompat.START)
        }
        binding.navView.getHeaderView(0).apply {
            findViewById<ImageView>(R.id.nav_drawer_image)?.load(imageStore.getNavDrawerImage())
        }
        val color = ContextCompat.getColorStateList(
            this,
            R.color.nav_drawer_menu_icon_tint
        )
        binding.navView.apply {
            // itemTextColor = color
            itemIconTintList = color
        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.navDrawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.favorites -> {
                    onDrawerCallback {
                        navigateTo(FavoriteActivity::class.java)
                    }
                    true
                }
                R.id.share -> {
                    onDrawerCallback {
                        share(
                            "${getString(R.string.app_share_message)}\n\nhttps://play.google.com/store/apps/details?id=${packageName}",
                            "Share"
                        )
                    }
                    true
                }
                R.id.rate -> {
                    onDrawerCallback {
                        rateApp()
                    }
                    true
                }
                R.id.more_apps -> {
                    onDrawerCallback {
                        moreApps()
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun setupNavigationDrawer() {
        binding.navDrawer.addDrawerListener(toggle)
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)
        toggle.syncState()
        binding.navDrawer.addDrawerListener(
            object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

                override fun onDrawerOpened(drawerView: View) {}

                override fun onDrawerClosed(drawerView: View) {
                    drawerCallback?.invoke()
                    drawerCallback = null
                }

                override fun onDrawerStateChanged(newState: Int) {}
            }
        )
    }
}