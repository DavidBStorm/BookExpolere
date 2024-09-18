package com.fara.bookexplorer.data.repository

import com.fara.bookexplorer.data.services.BookService
import com.fara.bookexplorer.domain.model.Book


import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookService: BookService
) {

    suspend fun searchBooks(query: String): List<Book>? {
        val response = bookService.searchBooks(query)
        if (response.isSuccessful) {
            return response.body()?.books
        }
        return null
    }
}


