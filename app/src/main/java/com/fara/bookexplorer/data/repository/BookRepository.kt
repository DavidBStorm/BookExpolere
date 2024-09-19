package com.fara.bookexplorer.data.repository

import com.fara.bookexplorer.data.services.BookService
import com.fara.bookexplorer.domain.model.BookResponse


import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookService: BookService
) {

    suspend fun searchBooks(query: String): BookResponse? {
        val response = bookService.searchBooks(query)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}


