package com.fara.bookexplorer.data.repository

import com.fara.bookexplorer.data.services.BookService
import com.fara.bookexplorer.domain.model.BookResponse
import com.fara.bookexplorer.data.pagination.BookPagingSource.Companion.LIMIT
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexplorer.data.pagination.BookPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class BookRepository @Inject constructor(
    private val bookService: BookService
) {

    fun searchBooks(query: String): Flow<PagingData<Doc>> {
        return Pager(
            config = PagingConfig(
                pageSize = BookPagingSource.LIMIT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BookPagingSource(bookService, query) }
        ).flow
    }
}


