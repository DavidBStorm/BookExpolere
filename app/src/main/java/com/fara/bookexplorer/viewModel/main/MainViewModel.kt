@file:OptIn(FlowPreview::class)
package com.fara.bookexplorer.viewModel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexplorer.domain.usecase.SearchBooksUseCase
import com.fara.bookexplorer.ui.state.MainIntent
import com.fara.bookexplorer.ui.state.MainUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<MainUIState>(MainUIState.Idle)
    val uiState: StateFlow<MainUIState> = _uiState

    private val _searchQuery = MutableStateFlow<String>("")


    init {
        // Process debounced search query changes
        viewModelScope.launch {
            _searchQuery
                .debounce(750) // Wait for 500ms after the last change
                .filter { it.length > 3 } // Only search if the query has more than 3 characters
                .distinctUntilChanged() // Avoid processing the same query again
                .collect { query ->
                    processIntent(MainIntent.Search(query))
                }
        }
    }

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Search -> handleSearch(intent.query)
            MainIntent.LoadMore -> handleLoadMore()
        }
    }

    fun onQueryTextChanged(query: String) {
        _searchQuery.value = query
    }

    private fun handleSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = MainUIState.Loading
            searchBooksUseCase(query)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _uiState.value = MainUIState.Success(pagingData)
                }
        }
    }

    // Handle pagination (load more)
    private fun handleLoadMore() {
        // Paging 3, loading more data is handled automatically by the library
    }
}
