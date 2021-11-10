package com.sarftec.lessonsinlife.datasource

import android.content.Context
import com.google.firebase.storage.FirebaseStorage
import com.sarftec.lessonsinlife.presentation.model.PictureQuote
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseImageDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageDataSource {

    private val ref = FirebaseStorage.getInstance().reference

    private var imageFileMap = hashMapOf<Int, ImageContent>()

    private suspend fun fetchImages() {
        ref.child(IMAGES_FOLDER)
            .listAll()
            .await()
            .items
            .mapIndexed { index, storageRef ->
                imageFileMap.put(index, ImageContent(storageRef.name))
            }
    }

    private suspend fun downloadImageFile(imageContent: ImageContent): File {
        val imageFile = File(context.cacheDir, imageContent.name)
        ref.child("$IMAGES_FOLDER/${imageContent.name}")
            .getFile(imageFile)
            .await()
        imageContent.fileLocation = imageFile.path
        return imageFile
    }

    private suspend fun fetchImageFile(imageContent: ImageContent) : File {
        return imageContent.fileLocation
            ?.let { filePath -> File(filePath).takeIf { it.exists() } }
            ?: downloadImageFile(imageContent)
    }

    override suspend fun getInitialKey(): Int {
        return 0
    }

    override suspend fun getKeySize(): Result<Int> {
        if (imageFileMap.keys.isEmpty()) fetchImages()
        return try {
            Result.success(imageFileMap.keys.size)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun getImageFile(key: Int): Result<PictureQuote> {
        if (imageFileMap.keys.isEmpty()) fetchImages()
        return try {
            val imageContent = imageFileMap[key]
                ?: throw Exception("Can't find image with key => $key")
            val result = fetchImageFile(imageContent)
            Result.success(PictureQuote(result, key))
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    class ImageContent(
        val name: String,
        var fileLocation: String? = null
    )

    companion object {
        const val IMAGES_FOLDER = "picture_images"
    }
}