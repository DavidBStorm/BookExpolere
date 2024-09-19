package com.fara.bookexplorer.ui.state

import androidx.paging.PagingData
import com.fara.bookexplorer.domain.model.BookResponse
import com.fara.bookexplorer.domain.model.Doc

sealed class MainUIState {
    object Loading : MainUIState()
    data class Success(val books: PagingData<Doc>) : MainUIState()
    data class Error(val message: String) : MainUIState()
    object Idle : MainUIState() // Optional: Initial state
}

sealed class MainIntent {
    data class Search(val query: String) : MainIntent()
    object LoadMore : MainIntent()
}


