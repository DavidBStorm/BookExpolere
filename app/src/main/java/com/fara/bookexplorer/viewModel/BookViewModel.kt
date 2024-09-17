package com.fara.bookexplorer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fara.bookexplorer.data.repository.BookRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    fun searchBooks(query: String) {
        viewModelScope.launch {
            val books = bookRepository.searchBooks(query)
            // Handle the books (update UI, etc.)
        }
    }
}
