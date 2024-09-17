package com.fara.bookexplorer.data.holder

import com.fara.bookexplorer.domain.model.Book
import com.fara.bookexplorer.domain.model.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("search.json")
    suspend fun searchBooks(@Query("title") title: String): Response<BookResponse>
}