package com.sarftec.lessonsinlife.presentation.activity

import android.app.WallpaperManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.widget.ViewPager2
import com.sarftec.lessonsinlife.databinding.ActivityPictureQuoteBinding
import com.sarftec.lessonsinlife.presentation.adapter.PictureQuoteDetailAdapter
import com.sarftec.lessonsinlife.presentation.dialog.PictureWallpaper
import com.sarftec.lessonsinlife.presentation.handler.ReadWriteHandler
import com.sarftec.lessonsinlife.presentation.parcel.MainToPicture
import com.sarftec.lessonsinlife.presentation.viewmodel.PictureQuoteDetailViewModel
import com.sarftec.lessonsinlife.utils.savePicture
import com.sarftec.lessonsinlife.utils.shareImage
import com.sarftec.lessonsinlife.utils.toast
import com.sarftec.lessonsinlife.utils.viewInGallery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@AndroidEntryPoint
class PictureQuoteActivity : BaseActivity() {

    private val layoutBinding by lazy {
        ActivityPictureQuoteBinding.inflate(
            layoutInflater
        )
    }

    private val pictureQuoteDetailAdapter by lazy {
        PictureQuoteDetailAdapter(imageStore) {
            viewModel.bindPictureQuote(it)
        }
    }

    private val pictureWallpaperDialog by lazy {
        PictureWallpaper(layoutBinding.root) {
           viewModel.getCurrentPictureQuote()?.let {
               val wallpaperManager = WallpaperManager.getInstance(this)
               wallpaperManager.setStream(it.file.inputStream())
               toast("Image set as wallpaper")
           }
        }
    }

    private val viewModel by viewModels<PictureQuoteDetailViewModel>()

    private lateinit var readWriteHandler: ReadWriteHandler

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutBinding.root)
        statusColor(Color.BLACK)
        configureHandlers()
        setupButtons()
        layoutBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
        getParcelFromIntent<MainToPicture>(intent)?.let {
            viewModel.setParcel(it)
        }
        layoutBinding.viewPager.adapter = pictureQuoteDetailAdapter
        viewModel.imageFlow?.let { pagingData ->
            lifecycleScope.launchWhenCreated {
                pagingData.collectLatest { pictureQuoteDetailAdapter.submitData(it) }
            }
        }
        layoutBinding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.setCurrentPosition(position)
                }
            }
        )
    }

    private fun configureHandlers() {
        readWriteHandler = ReadWriteHandler(this)
    }


    private fun setupButtons() {
        layoutBinding.setWallpaper.setOnClickListener {
            pictureWallpaperDialog.show()
        }
        layoutBinding.sharePicture.setOnClickListener { _ ->
            viewModel.getCurrentPictureQuote()?.let {
                shareImage(it.file)
            }
        }
        layoutBinding.downloadPicture.setOnClickListener {
            saveFileToGallery(viewModel.getCurrentPictureQuote()?.file)
        }
    }

    private fun saveFileToGallery(file: File?) {
        readWriteHandler.requestReadWrite {
            savePicture { uri, outputStream ->
                if (file == null || outputStream == null) return@savePicture
                try {
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    toast("Image saved to gallery")
                    uri?.let { viewInGallery(it) }
                } catch (e: Exception) {
                    toast("Error occurred!. Try again")
                }
            }
        }
    }
}