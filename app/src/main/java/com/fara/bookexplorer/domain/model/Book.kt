package com.fara.bookexplorer.domain.model

import com.google.gson.annotations.SerializedName


data class BookResponse(
    @SerializedName("docs") val books: List<Book>
)

data class Book(
    @SerializedName("key") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("cover_i") val coverId: Int?
)
