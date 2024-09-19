package com.fara.bookexplorer.domain.model


import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("author_alternative_name")
    val authorAlternativeName: List<String> = listOf(),
    @SerializedName("author_facet")
    val authorFacet: List<String> = listOf(),
    @SerializedName("author_key")
    val authorKey: List<String> = listOf(),
    @SerializedName("author_name")
    val authorName: List<String> = listOf(),
    @SerializedName("contributor")
    val contributor: List<String> = listOf(),
    @SerializedName("ebook_access")
    val ebookAccess: String = "",
    @SerializedName("ebook_count_i")
    val ebookCountI: Int = 0,
    @SerializedName("edition_count")
    val editionCount: Int = 0,
    @SerializedName("edition_key")
    val editionKey: List<String> = listOf(),
    @SerializedName("first_publish_year")
    val firstPublishYear: Int = 0,
    @SerializedName("has_fulltext")
    val hasFulltext: Boolean = false,
    @SerializedName("key")
    val key: String = "",
    @SerializedName("language")
    val language: List<String> = listOf(),
    @SerializedName("last_modified_i")
    val lastModifiedI: Int = 0,
    @SerializedName("number_of_pages_median")
    val numberOfPagesMedian: Int = 0,
    @SerializedName("oclc")
    val oclc: List<String> = listOf(),
    @SerializedName("public_scan_b")
    val publicScanB: Boolean = false,
    @SerializedName("publish_date")
    val publishDate: List<String> = listOf(),
    @SerializedName("publish_place")
    val publishPlace: List<String> = listOf(),
    @SerializedName("publish_year")
    val publishYear: List<Int> = listOf(),
    @SerializedName("publisher")
    val publisher: List<String> = listOf(),
    @SerializedName("publisher_facet")
    val publisherFacet: List<String> = listOf(),
    @SerializedName("seed")
    val seed: List<String> = listOf(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("title_sort")
    val titleSort: String = "",
    @SerializedName("title_suggest")
    val titleSuggest: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("_version_")
    val version: Long = 0
)