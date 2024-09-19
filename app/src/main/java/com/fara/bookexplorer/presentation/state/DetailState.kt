package com.fara.bookexplorer.presentation.state

import com.fara.bookexplorer.domain.model.Doc

sealed class DetailState {
    data class ShowBookDetails(val book: Doc) : DetailState()
    data class Error(val message: String) : DetailState()
}

sealed class DetailIntent {
    object LoadBookDetails : DetailIntent()
}

