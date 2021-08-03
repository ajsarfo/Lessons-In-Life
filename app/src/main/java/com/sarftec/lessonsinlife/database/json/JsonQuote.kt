package com.sarftec.lessonsinlife.database.json

import com.sarftec.lessonsinlife.database.model.Quote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class JsonQuote(
    @SerialName("category_id") val categoryId: String,
    @SerialName("id")val id: Int,
    @SerialName("quote")val quote: String
) {
    fun toQuote() : Quote {
        return Quote(id = id, categoryId = categoryId.toInt(), message = quote)
    }
}