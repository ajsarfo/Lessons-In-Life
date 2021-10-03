package com.sarftec.lessonsinlife.presentation.activity

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.sarftec.lessonsinlife.R
import com.sarftec.lessonsinlife.advertisement.AdCountManager
import com.sarftec.lessonsinlife.advertisement.BannerManager
import com.sarftec.lessonsinlife.databinding.ActivityQuoteDetailBinding
import com.sarftec.lessonsinlife.databinding.LayoutTextPanelBinding
import com.sarftec.lessonsinlife.presentation.adapter.DetailImageAdapter
import com.sarftec.lessonsinlife.presentation.adapter.QuotePagerAdapter
import com.sarftec.lessonsinlife.presentation.dialog.ImageChooser
import com.sarftec.lessonsinlife.presentation.handler.ImageHandler
import com.sarftec.lessonsinlife.presentation.handler.ReadWriteHandler
import com.sarftec.lessonsinlife.presentation.panel.TextPanelManager
import com.sarftec.lessonsinlife.presentation.viewmodel.BackgroundOption
import com.sarftec.lessonsinlife.presentation.viewmodel.QuoteDetailViewModel
import com.sarftec.lessonsinlife.utils.copy
import com.sarftec.lessonsinlife.utils.toBitmap
import com.sarftec.lessonsinlife.utils.toast
import com.sarftec.lessonsinlife.utils.vibrate
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class QuoteDetailActivity : BaseActivity(), ColorPickerDialogListener {

    private val binding by lazy {
        ActivityQuoteDetailBinding.inflate(layoutInflater)
    }

    private val quotePagerAdapter by lazy {
        QuotePagerAdapter {
            textPanelManager.dismiss()
        }
    }

    private lateinit var readWriteHandler: ReadWriteHandler

    private lateinit var imageHandler: ImageHandler

    private val imageChooserDialog by lazy {
        ImageChooser(
            binding.root,
            DetailImageAdapter(imageStore) { uri ->
                viewModel.setBackgroundImage(uri)
                dismissImageChooser()
            }
        )
    }

    private val textPanelManager by lazy {
        TextPanelManager(
            lifecycleScope,
            viewModel,
            LayoutTextPanelBinding.inflate(
                layoutInflater,
                binding.textPanelContainer,
                true
            )
        )
    }

    private val viewModel by viewModels<QuoteDetailViewModel>()

    override fun createAdCounterManager(): AdCountManager {
        return addCounter
    }
    override fun onBackPressed() {
        interstitialManager?.showAd {
            super.onBackPressed()
        } ?: let { super.onBackPressed() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        setContentView(binding.root)
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_quote),
            binding.mainBanner
        )
        /**********************************************************/
        savedInstanceState ?: kotlin.run {
            intent.getBundleExtra(ACTIVITY_BUNDLE)?.let {
                viewModel.setBundle(it)
            }
        }
        configurePanelAndBottomView()
        configureViewPager()
        configureHandlers()
        //fetchRandomBackground()
        observePanelState()
        viewModel.fetch()
        viewModel.indexedQuotes.observe(this) {
            quotePagerAdapter.submitData(it.quotes)
            binding.viewPager.setCurrentItem(it.index, false)
        }
        viewModel.currentQuote.observe(this) {
            binding.detailBottom.favoriteIcon.setImageResource(
                if(it.isFavorite) R.drawable.ic_love_filled else R.drawable.ic_love_borderless
            )
        }
    }

    private fun configureHandlers() {
        readWriteHandler = ReadWriteHandler(this)
        imageHandler = ImageHandler(this)
    }

    private fun observePanelState() {
        viewModel.panelState.observe(this) { state ->
            if (state.opacity != -1) binding.view.setBackgroundColor(state.opacity)
            makeBackgroundChanges(state.backgroundOption)
            quotePagerAdapter.changePanelState(state)
        }

        viewModel.background.observe(this) { state ->
            state.color?.let { color ->
                binding.background.setImageDrawable(
                    ColorDrawable(color)
                )
            }
            state.image?.let {
                val uri = (Uri.parse(it))
                binding.background.load(uri, imageStore.imageLoader) {
                    allowHardware(false)
                }
            }
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

  /*
    private fun fetchRandomBackground() {
        vibrate()
        binding.background.load(imageStore.randomQuoteBackground())
    }
   */

    private fun configurePanelAndBottomView() {
        binding.detailBottom.changeBackground.setOnClickListener {
            vibrate()
            viewModel.randomBackground()
          // fetchRandomBackground()
        }

        binding.detailBottom.favorite.setOnClickListener { _ ->
            viewModel.changeCurrentQuoteFavorite()?.let {
                vibrate()
                toast(if(it.isFavorite) "Added to Favorites" else "Removed from favorites")
            }
        }

        binding.detailBottom.showPanel.setOnClickListener {
            vibrate()
            textPanelManager.show()
        }

        binding.detailBottom.copy.setOnClickListener { _ ->
            viewModel.getCurrentQuote()?.let {
                vibrate()
                toast("Copied to clipboard")
                copy(it.message, "label")
            }
        }

        binding.detailBottom.download.setOnClickListener { _ ->
            vibrate()
            val imageName = "temp_image"
            val file = File.createTempFile(imageName, ".jpg", cacheDir)
            file.outputStream().use { outputStream ->
                binding.captureFrame.toBitmap {
                    val compressed = it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    if (compressed) outputStream.flush()
                }
            }
            interstitialManager?.showAd {
                navigateTo(
                    PreviewActivity::class.java,
                    bundle = Bundle().apply {
                        putString("imageName", file.name)
                    }
                )
            }
        }
    }

    //Trivial
    private fun dismissImageChooser() {
        imageChooserDialog.dismiss()
    }

    private fun makeBackgroundChanges(option: BackgroundOption) {
        when (option) {
            BackgroundOption.COLOR -> {
                ColorPickerDialog
                    .newBuilder()
                    .show(this)
                viewModel.neutralizeBackgroundOption()
            }
            BackgroundOption.GALLERY -> {
                imageHandler.pickImageFromGallery { isSuccess, uri ->
                    if (isSuccess && uri != null) viewModel.setBackgroundImage(uri)
                }
                viewModel.neutralizeBackgroundOption()
            }
            BackgroundOption.IMAGES -> {
                imageChooserDialog.show()
                viewModel.neutralizeBackgroundOption()
            }
            else -> {
                //Nothing
            }
        }
    }
            /**
     * Jaredrummler color picker listeners
     */
    override fun onColorSelected(dialogId: Int, color: Int) {
        viewModel.setBackgroundColor(color)
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    companion object {
        private val addCounter = AdCountManager(listOf(2, 5))
    }
}