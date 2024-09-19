package com.fara.bookexplorer.presentation.fragments.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.fara.bookexplorer.domain.model.Doc
import com.fara.bookexplorer.presentation.base.BaseFragment
import com.fara.bookexplorer.presentation.state.DetailIntent
import com.fara.bookexplorer.presentation.state.DetailState
import com.fara.bookexplorer.presentation.viewModel.detail.DetailViewModel
import com.fara.bookexpolorer.R
import com.fara.bookexpolorer.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {
    private var book: Doc? = null
    private lateinit var viewModel: DetailViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        handleItemsAndObserve()


    }

    private fun handleItemsAndObserve() {
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

    private fun initView() {
        arguments?.let {
            book = it.getParcelable(ARG_BOOK)
        }

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }

    private fun showBookDetails(book: Doc) {
        binding.bookTitle.text = getString(R.string.title, book.title)
        binding.bookAuthor.text = getString(R.string.author, book.authorName.joinToString(", "))
        binding.bookYear.text = getString(R.string.year, book.getFirstPublishYearV2())
        binding.bookLanguage.text =
            getString(R.string.language, book.language.firstOrNull() ?: "Unknown")
        binding.bookLastModified.text = getString(R.string.last_modified, book.lastModifiedI.toString())



        binding.bookCoverImage.load(book.getCoverImageUrl()) {
            placeholder(R.drawable.placeholder)
            error(R.drawable.error)
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