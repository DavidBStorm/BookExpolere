package com.fara.bookexplorer.ui.state

import com.fara.bookexplorer.domain.model.BookResponse

sealed class MainUIState {
    object Idle : MainUIState()
    object Loading : MainUIState()
    data class Success(val books: BookResponse?) : MainUIState()
    data class Error(val message: String) : MainUIState()
}
