package com.fara.bookexplorer.viewModel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fara.bookexplorer.data.repository.BookRepository
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexplorer.ui.state.DetailIntent
import com.fara.bookexplorer.ui.state.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {


        private val _uiState = MutableStateFlow<DetailState>(DetailState.Error("No data"))
        val uiState: StateFlow<DetailState> = _uiState

        fun processIntent(intent: DetailIntent, book: Doc?) {
            when (intent) {
                is DetailIntent.LoadBookDetails -> {
                    if (book != null) {
                        _uiState.value = DetailState.ShowBookDetails(book)
                    } else {
                        _uiState.value = DetailState.Error("Book details not found")
                    }
                }
            }
        }
}


