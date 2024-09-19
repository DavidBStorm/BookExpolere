package com.fara.bookexplorer.domain.usecase

import com.fara.bookexplorer.data.repository.BookRepository
import com.fara.bookexplorer.domain.model.BookResponse
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(query: String,page:Int): BookResponse? {
        return bookRepository.searchBooks(query,page)
    }
}
