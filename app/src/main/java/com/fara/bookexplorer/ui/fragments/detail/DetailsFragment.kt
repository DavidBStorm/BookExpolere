package com.fara.bookexplorer.ui.fragments.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexplorer.ui.base.BaseFragment
import com.fara.bookexplorer.ui.state.DetailIntent
import com.fara.bookexplorer.ui.state.DetailState
import com.fara.bookexplorer.viewModel.detail.DetailViewModel
import com.fara.bookexplorer.viewModel.main.MainViewModel
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {
    private var book: Doc? = null
    private lateinit var viewModel: DetailViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            book = it.getParcelable(ARG_BOOK)
        }

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]


        viewModel.processIntent(DetailIntent.LoadBookDetails, book)


        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is DetailState.ShowBookDetails -> showBookDetails(state.book)
                    is DetailState.Error -> showError(state.message)
                }
            }
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }

    private fun showBookDetails(book: Doc) {
        Log.e("eee", "showBookDetails: ${book.getCoverImageUrl()}" )
        binding.bookTitle.text = "Title: ${book.title}"
        binding.bookAuthor.text = "Author: ${book.authorName.joinToString(", ")}"
        binding.bookYear.text = "Year: ${book.getFirstPublishYearV2()}"
        binding.bookLanguage.text = "Language: ${book.language.firstOrNull() ?: "Unknown"}"
        binding.bookLastModified.text = "Last Modified: ${book.lastModifiedI}"


        val coverId = book.coverID
        if (coverId != null) {

            binding.bookCoverImage.load(book.getCoverImageUrl()) {
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
            }
        } else {
            // Handle if no image is available
            binding.bookCoverImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_BOOK = "arg_book"

        fun newInstance(book: Doc): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle().apply {
                putParcelable(ARG_BOOK, book)
            }
            fragment.arguments = args
            return fragment
        }
    }
}