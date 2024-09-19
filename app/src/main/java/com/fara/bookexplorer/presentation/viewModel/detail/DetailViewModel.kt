package com.fara.bookexplorer.presentation.viewModel.detail

import androidx.lifecycle.ViewModel
import com.fara.bookexplorer.data.repository.BookRepository
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexplorer.presentation.state.DetailIntent
import com.fara.bookexplorer.presentation.state.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {


        private val _uiState = MutableStateFlow<DetailState>(DetailState.Error(NO_DATA))
        val uiState: StateFlow<DetailState> = _uiState

        fun processIntent(intent: DetailIntent, book: Doc?) {
            when (intent) {
                is DetailIntent.LoadBookDetails -> {
                    if (book != null) {
                        _uiState.value = DetailState.ShowBookDetails(book)
                    } else {
                        _uiState.value = DetailState.Error(ERROR_EVENT)
                    }
                }
            }
        }

    companion object{
        const val ERROR_EVENT ="Book details not found"
        const val NO_DATA ="No data"
    }
}


