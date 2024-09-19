package com.fara.bookexplorer.viewModel.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fara.bookexplorer.data.repository.BookRepository
import com.fara.bookexplorer.domain.model.BookResponse
import com.fara.bookexplorer.domain.usecase.SearchBooksUseCase
import com.fara.bookexplorer.ui.state.MainUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase
) : ViewModel() {

    // To hold UI state
    private val _uiState = MutableStateFlow<MainUIState>(MainUIState.Idle)
    val uiState: StateFlow<MainUIState> = _uiState

    // To collect search queries with debounce
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Pagination state
    private var currentPage = 1
    internal var isLastPage = false
    internal var isLoading = false

    init {
        // Collect search queries with debounce
        viewModelScope.launch {
            _searchQuery
                .debounce(500) // Wait 500ms after the last input
                .collect { query ->
                    if (query.isNotEmpty()) {
                        currentPage = 1
                        isLastPage = false
                        searchBooks(
                            query,
                            currentPage
                        ) // Handle the business logic through use case
                    }
                }
        }
    }

    // Update the query state
    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // Handle book search and state emission
    private fun searchBooks(query: String, page: Int) {
        if (isLoading || isLastPage) return

        viewModelScope.launch {
            _uiState.value = MainUIState.Loading // Emit loading state
            isLoading = true

            try {
                val newBooks = searchBooksUseCase.invoke(query, page)
                isLoading = false

                val newDocs = newBooks?.docs.orEmpty()
                if (newDocs.isEmpty()) {
                    isLastPage = true
                }

                _uiState.value = if (page == 1) {
                    // For the first page, simply set the new results
                    MainUIState.Success(newBooks)
                } else {
                    // For subsequent pages, append new results to existing results
                    val currentBooks =
                        (_uiState.value as? MainUIState.Success)?.books ?: BookResponse()
                    val updatedDocs = currentBooks.docs + newDocs
                    val updatedBooks = currentBooks.copy(docs = updatedDocs)
                    MainUIState.Success(updatedBooks)
                }

                currentPage++
            } catch (e: Exception) {
                _uiState.value = MainUIState.Error(e.message ?: "Unknown Error") // Emit error state
            }
        }
    }


    // Method to load more books (for pagination)
    fun loadMoreBooks() {
        if (!isLoading && !isLastPage) {
            searchBooks(_searchQuery.value, currentPage)
        }
    }
}

