@file:OptIn(FlowPreview::class)
package com.fara.bookexplorer.presentation.viewModel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fara.bookexplorer.domain.usecase.SearchBooksUseCase
import com.fara.bookexplorer.presentation.state.MainIntent
import com.fara.bookexplorer.presentation.state.MainUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<MainUIState>(MainUIState.Idle)
    val uiState: StateFlow<MainUIState> = _uiState

    private val _searchQuery = MutableStateFlow("")


    init {

        viewModelScope.launch {
            _searchQuery
                .debounce(LIMIT_TIME)
                .filter { it.length > LIMIT_CHAR }
                .distinctUntilChanged()
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


    private fun handleLoadMore() {
        // Paging 3, loading more data is handled automatically by the library
    }

    companion object {
        const val LIMIT_TIME = 750L
        const val LIMIT_CHAR = 3

    }
}
