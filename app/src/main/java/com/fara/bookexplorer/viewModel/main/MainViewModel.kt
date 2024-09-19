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

    init {
        // Collect search queries with a debounce
        viewModelScope.launch {
            _searchQuery
                .debounce(500) // Wait 500ms after the last input
                .collect { query ->
                    if (query.isNotEmpty()) {
                        searchBooks(query) // Handle the business logic through use case
                    }
                }
        }
    }

    // Update the query state
    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // Handle book search and state emission
    private suspend fun searchBooks(query: String) {
        _uiState.value = MainUIState.Loading // Emit loading state
        try {
            val books = searchBooksUseCase(query)
            _uiState.value = MainUIState.Success(books) // Emit success state
        } catch (e: Exception) {
            _uiState.value = MainUIState.Error(e.message ?: "Unknown Error") // Emit error state
        }
    }
}

