package com.fara.bookexplorer.viewModel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fara.bookexplorer.data.repository.BookRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {


}
