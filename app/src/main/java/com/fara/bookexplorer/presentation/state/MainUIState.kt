package com.fara.bookexplorer.presentation.state

import androidx.paging.PagingData
import com.fara.bookexplorer.domain.model.Doc

sealed class MainUIState {
    object Loading : MainUIState()
    data class Success(val books: PagingData<Doc>) : MainUIState()
    data class Error(val message: String) : MainUIState()
    object Idle : MainUIState()
}

sealed class MainIntent {
    data class Search(val query: String) : MainIntent()
    object LoadMore : MainIntent()
}


