package com.sarftec.lessonsinlife.database.json

import com.sarftec.lessonsinlife.database.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class JsonCategory(
    @SerialName("category_id") val categoryId: String,
    @SerialName("category_name")val categoryName: String,
    @SerialName("id")val id: Int
) {
    fun toCategory() : Category {
        return Category(categoryId.toInt(), categoryName)
    }
}