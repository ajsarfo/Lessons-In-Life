package com.sarftec.lessonsinlife.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.sarftec.lessonsinlife.datasource.ImageDataSource
import com.sarftec.lessonsinlife.paging.ImagePagingSource
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import com.sarftec.lessonsinlife.presentation.parcel.MainToPicture
import com.sarftec.lessonsinlife.repository.impl.ImageRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PictureQuoteDetailViewModel @Inject constructor(
    private val imageDataSource: ImageDataSource,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private var parcel: MainToPicture? = null

    private var keyTracker: KeyTracker? = null

    private val pictureQuoteSet = hashSetOf<PictureQuote>()


    @ExperimentalPagingApi
    private val imageRepository by lazy {
        stateHandle.get<MainToPicture>(PARCEL)?.let {
            keyTracker = KeyTracker((it.position))
            ImageRepositoryImpl(
                ImagePagingSource(
                    CustomImageDataSource(it.position)
                )
            )
        }
    }


    @ExperimentalPagingApi
    val imageFlow by lazy {
        imageRepository?.getImageFlow()
    }

    fun setParcel(parcel: MainToPicture) {
        this.parcel = parcel
        stateHandle.set(PARCEL, parcel)
    }

    fun bindPictureQuote(pictureQuote: PictureQuote) {
        pictureQuoteSet.add(pictureQuote)
    }

    fun setCurrentPosition(position: Int) {
       // Log.v("TAT", "position $position")
       keyTracker?.move(position)
    }

    fun getCurrentPictureQuote() : PictureQuote? {
        val key = keyTracker?.currentKey ?: return null
        Log.v("TAT", "key $key")
        return pictureQuoteSet.find { it.key == key }
    }

    inner class CustomImageDataSource(private val initialKey: Int) :
        ImageDataSource by imageDataSource {
        override suspend fun getInitialKey(): Int {
            return initialKey
        }
    }

    class KeyTracker(var currentKey: Int) {
        private var positionToKey = 0
        private var ignorekey = true

        fun move(position: Int) {
            if(position > positionToKey) {
               if(!ignorekey) {
                   currentKey++
                   positionToKey++
                   Log.v("TAT", "Moving: key => $currentKey , position => $positionToKey")
               }
                ignorekey = false
            }
            else if(position < positionToKey) {
                if(!ignorekey) {
                    currentKey--
                    positionToKey--
                    Log.v("TAT", "Moving: key => $currentKey , position => $positionToKey")
                }
                ignorekey = false
            }
        }
    }

    companion object {
        private const val PARCEL = "parcel"
    }
}