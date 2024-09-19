package com.fara.bookexplorer.domain.usecase

import com.fara.bookexplorer.data.repository.BookRepository
import com.fara.bookexplorer.domain.model.BookResponse
import javax.inject.Inject
import androidx.paging.PagingData
import com.fara.bookexplorer.domain.model.Doc
import kotlinx.coroutines.flow.Flow

class SearchBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Doc>> {
        return bookRepository.searchBooks(query)
    }
}

